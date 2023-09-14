package com.example.backend.controller;


import com.example.backend.config.Configuration;
import com.example.backend.entity.RestBean;

import com.example.backend.utils.Utils;
import com.fasterxml.uuid.Generators;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.Data;
import net.sf.jmimemagic.*;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.ibatis.jdbc.Null;
import org.bouncycastle.jce.exception.ExtIOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
class Reader {
  private String content;
}

@Data
class Catalogue {
  private Integer chapter;
  private String title;
  private List<Catalogue> children;
  private String mime;
  private String content;
}

@RestController
public class UploadController {
  @PostMapping(value = "/api/upload", consumes = "multipart/form-data")
  public RestBean<String> upload(MultipartFile file) throws IOException, ServerException, MagicMatchNotFoundException, InsufficientDataException, MagicException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, MagicParseException, InvalidResponseException, XmlParserException, InternalException {
    // 例子 https://juejin.cn/post/7026282079356715016
    // 文档 http://www.siegmann.nl/epublib/example-programs/read-epub-file
    // api https://min.io/docs/minio/linux/developers/java/API.html

    File f = Utils.MultipartToFile(file);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    UUID uuid = Generators.timeBasedEpochGenerator().generate();
    String filename = f.getName();
    String ext = filename.substring(filename.lastIndexOf("."));
    // 日期 + 文件夹路径 + 文件路径以及相关的资源文件
    String path = date + "/" + uuid + "/" + uuid + ext;
    String url = new StringBuilder().append(Configuration.URL).append(Configuration.bucket).append("/") + path;
    Magic magic = new Magic();
    MagicMatch match = magic.getMagicMatch(f, false);
    Utils.upload(new FileInputStream(f), path, match.getMimeType());

    return RestBean.success(url, "成功");
  }

  @PostMapping("/api/reader/parse")
  public RestBean<Object> parse() throws IOException, ServerException, MagicMatchNotFoundException, InsufficientDataException, MagicException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, MagicParseException, InvalidResponseException, XmlParserException, InternalException {
    File f = new File("C:\\Users\\last order\\Documents\\Tencent Files\\2495713984\\FileRecv\\epub 小说 禁传\\[榎宫佑][台／简&精排] NO GAME NO LIFE游戏人生 10 游戏玩家兄妹似乎被迫为过去付出代价.epub");

    InputStream in;
    //从输入流当中读取epub格式文件
    EpubReader epub = new EpubReader();
    in = new FileInputStream(f);
    Book book = epub.readEpub(in);
    //获取到书本的头部信息
    Metadata metadata = book.getMetadata();
    System.out.println(metadata.getMetaAttribute("cover"));
    System.out.println("FirstTitle为：" + metadata.getFirstTitle());
    //获取到书本的全部资源
    Resources resources = book.getResources();
    System.out.println("所有资源数量为：" + resources.size());
    //获取所有的资源数据
//    Collection<String> allHrefs = resources.getAllHrefs();
    //获取到书本的内容
    List<Resource> contents = book.getContents();
    System.out.println("内容资源数量为：" + contents.size());
    Map sourceMap = new HashMap<String, byte[]>();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    UUID uuid = Generators.timeBasedEpochGenerator().generate();


    List<Resource> filter = resources.getAll().stream().filter((item) -> {
      String key = item.getHref();
      return !key.endsWith(".xhtml") && !key.endsWith(".css");
    }).toList();

    for (Resource item : filter) {
      String mime = item.getMediaType().toString();
      String key = item.getHref();
      int lastIndex = key.lastIndexOf(".");
      String ext = "";
      if (lastIndex != -1) {
        ext = key.substring(key.lastIndexOf("."));
      }
      String filename = Generators.timeBasedEpochGenerator().generate().toString();
      String path = date + "/" + uuid + "/" + filename + ext;
      String url = new StringBuilder().append(Configuration.URL).append(Configuration.bucket).append("/") + path;
      Utils.upload(item.getInputStream(), path, mime);
      sourceMap.put(key, url);
    }

    List<Resource> cssList = resources.getAll().stream().filter((item) -> {
      return item.getHref().endsWith(".css");
    }).toList();

    for (Resource item : cssList) {
      String css = new String(item.getData());

      String reg2 = "url\\(['\"]?([^'\"\\)]+)['\"]?\\)";
//      var result = css.replaceAll(reg2)
//      System.out.println(result);
      Pattern pattern = Pattern.compile(reg2);
      Matcher matcher = pattern.matcher(css);

      while (matcher.find()) {
        // 当前匹配到的字符串
        System.out.println("匹配数量");
        System.out.println(matcher.groupCount());
        String first = matcher.group().substring(4);
        String result = first.substring(0, first.length() - 1);
        System.out.println(result);
        String[] path = result.split("/");
        String key = path[path.length - 1];

        String str = path[path.length - 2] + "/" + path[path.length - 1];
        var val = sourceMap.get(str);
        if (str.endsWith(key) && val != null) {
          String url = "url(" + "'" + val +  "'" + ")";
          css = css.replace(matcher.group(), url);
        }
      }
      String filename = Generators.timeBasedEpochGenerator().generate().toString();
      String path = date + "/" + uuid + "/" + filename + ".css";
      String url = new StringBuilder().append(Configuration.URL).append(Configuration.bucket).append("/") + path;
      Utils.upload(new ByteArrayInputStream(css.getBytes()), path, "text/css");
      sourceMap.put(item.getHref(), url);
      System.out.println(css);

//      System.out.println(css);
    }


    //获取到书本的spine资源 线性排序
    Spine spine = book.getSpine();
    System.out.println("spine资源数量为：" + spine.size());
    //通过spine获取所有的数据
    List<SpineReference> spineReferences = spine.getSpineReferences();

    List list = new ArrayList();
    for (SpineReference spineReference : spineReferences) {
      Resource spineReferenceResource = spineReference.getResource();
      //data就是资源的内容数据，可能是css,html,图片等等
      byte[] data = spineReferenceResource.getData();

      // 获取到内容的类型  css,html,还是图片
      MediaType mediaType = spineReferenceResource.getMediaType();
      System.out.print(mediaType.toString());
//      Object o = new Object();
      Reader reader = new Reader();
      Document doc = Jsoup.parse(new String(data));
      // html parser https://jsoup.org/cookbook/extracting-data/selector-syntax
      Elements img = doc.select("img");
      Elements link = doc.select("link");
      Elements script = doc.select("script");

      for (Element el : img) {
        String src = el.attr("src");
        String[] path = src.split("/");
        String key = path[path.length - 1];

        String str = path[path.length - 2] + "/" + path[path.length - 1];
        var val = sourceMap.get(str);
        if (src.endsWith(str) && val != null) {
          el.attr("src", val.toString());
        }
      }
      for (Element el : link) {
        String src = el.attr("href");
        String[] path = src.split("/");
        String key = path[path.length - 1];

        String str = path[path.length - 2] + "/" + path[path.length - 1];
        var val = sourceMap.get(str);
        if (src.endsWith(str)  && val != null) {
          el.attr("href", val.toString());
        }
      }
      for (Element el : script) {
        String src = el.attr("src");
        String[] path = src.split("/");
        String key = path[path.length - 1];

        String str = path[path.length - 2] + "/" + path[path.length - 1];
        var val = sourceMap.get(str);
        if (src.endsWith(str)  && val != null) {
          el.attr("src", val.toString());
        }
      }
      reader.setContent(doc.toString());
      list.add(reader);
    }

    //获取到书本的目录资源
    TableOfContents tableOfContents = book.getTableOfContents();
    System.out.print("目录资源数量为：" + tableOfContents.size());
    //获取到目录对应的资源数据
    List<TOCReference> tocReferences = tableOfContents.getTocReferences();
    List<Catalogue> result = getToc(tocReferences, new ArrayList<>());


    return RestBean.success(list, "成功");
  }

  private List<Catalogue> getToc(List<TOCReference> tocReferences, List<Catalogue> result) throws IOException {
    int chapter = 1;

    for (TOCReference tocReference : tocReferences) {
      Resource resource = tocReference.getResource();
      //data就是资源的内容数据，可能是css,html,图片等等
      String title = tocReference.getTitle();
      byte[] data = resource.getData();

      // 获取到内容的类型  css,html,还是图片
      MediaType mediaType = resource.getMediaType();
      Catalogue catalogue = new Catalogue();
      catalogue.setTitle(title);
      catalogue.setChapter(chapter++);
      catalogue.setMime(mediaType.toString());
      catalogue.setContent(new String(data));
      result.add(catalogue);

      if (tocReference.getChildren().size() > 0) {
        catalogue.setChildren(new ArrayList<>());
        getToc(tocReference.getChildren(), catalogue.getChildren());
      }
    }

    return result;
  }
}

