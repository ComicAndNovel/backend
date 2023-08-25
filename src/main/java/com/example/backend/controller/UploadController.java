package com.example.backend.controller;


import com.example.backend.config.MinIOConfig;
import com.example.backend.entity.RestBean;

import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import lombok.Data;
import net.sf.jmimemagic.*;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Data
class Reader {
  private String title;
  private Integer page;
  private String content;
  private String  mime;
}

@Data
class Catalogue {
  private Integer chapter;
  private String title;
}

@RestController
public class UploadController {
  @PostMapping(value = "/api/reader/upload", consumes = "multipart/form-data")
  public RestBean<List<Object>> upload () throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MagicMatchNotFoundException, MagicException, MagicParseException {
//    if (file ==)
    // 例子 https://juejin.cn/post/7026282079356715016
    // 文档 http://www.siegmann.nl/epublib/example-programs/read-epub-file

    File f = new File("C:\\Users\\last order\\Documents\\Tencent Files\\2495713984\\FileRecv\\[榎宫佑][台／简&精排] NO GAME NO LIFE游戏人生 01 听说游戏玩家兄妹要征服幻想世界.epub");
    FileInputStream in = null;

    MinIOConfig minIOConfig = new MinIOConfig();
    Magic magic = new Magic();
    MagicMatch match = magic.getMagicMatch(f, false);


    minIOConfig.getMinioClient()
      .putObject(
        PutObjectArgs.builder()
          .bucket(minIOConfig.bucket)
          .object(f.getName())
          .stream(new FileInputStream(f), -1, 10485760)
          .contentType(match.getMimeType())
          .build()
      );

    //从输入流当中读取epub格式文件
    EpubReader epub = new EpubReader();
    in = new FileInputStream(f);
    Book book = epub.readEpub(in);
    //获取到书本的头部信息
    Metadata metadata = book.getMetadata();
    System.out.println("FirstTitle为：" + metadata.getFirstTitle());
    //获取到书本的全部资源
    Resources resources = book.getResources();
    System.out.println("所有资源数量为：" + resources.size());
    //获取所有的资源数据
    Collection<String> allHrefs = resources.getAllHrefs();
//            //获取到书本的内容
    List<Resource> contents = book.getContents();
    System.out.println("内容资源数量为：" + contents.size());
    Map sourceMap = new HashMap<String, byte[]>();

    for (String href : allHrefs) {
        sourceMap.put(href, href.getBytes());
    }


    //获取到书本的spine资源 线性排序
    Spine spine = book.getSpine();
    System.out.println("spine资源数量为：" + spine.size());
    //通过spine获取所有的数据
    List<SpineReference> spineReferences = spine.getSpineReferences();
    List list = new ArrayList();
    for (SpineReference spineReference : spineReferences) {
      Resource resource = spineReference.getResource();
      //data就是资源的内容数据，可能是css,html,图片等等
      byte[] data = resource.getData();
      // 获取到内容的类型  css,html,还是图片
      MediaType mediaType = resource.getMediaType();
//      Object o = new Object();
      Reader reader = new Reader();
      reader.setContent(new String(data));

      list.add(reader);
    }

    List<Object> result = new ArrayList<>();
    //获取到书本的目录资源
    TableOfContents tableOfContents = book.getTableOfContents();
    System.out.println("目录资源数量为：" + tableOfContents.size());
    //获取到目录对应的资源数据
    List<TOCReference> tocReferences = tableOfContents.getTocReferences();

    int chapter = 0;

    for (TOCReference tocReference : tocReferences) {
      Resource resource = tocReference.getResource();
      //data就是资源的内容数据，可能是css,html,图片等等
      String title = tocReference.getTitle();
      System.out.println("title为 ======" + title);
      byte[] data = resource.getData();

      // 获取到内容的类型  css,html,还是图片
      MediaType mediaType = resource.getMediaType();
      List<TOCReference> children = tocReference.getChildren();
      Catalogue catalogue = new Catalogue();
      catalogue.setTitle(title);
      catalogue.setChapter(chapter++);


      result.add(catalogue);
      if (tocReference.getChildren().size() > 0) {
        //获取子目录的内容
      }
    }

    return RestBean.success(list, 0, list.size(), 0);
  }
}
