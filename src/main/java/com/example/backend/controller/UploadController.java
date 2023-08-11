package com.example.backend.controller;

import com.example.backend.entity.Reader;
import com.example.backend.entity.RestBean;

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
import java.util.*;

@RestController
public class UploadController {
    @PostMapping("/api/reader/upload")
    public RestBean<List<Reader>> upload (@RequestBody Map<String, Object> query) throws IOException {

//        System.out.println("文件名：" + file.getOriginalFilename());
//        System.out.println("文件大小：" + file.getSize());
//        System.out.println("文件内容：" + file.getBytes());
//        System.out.println(file);

        System.out.println("===== 调用 ======");
        File f = new File("C:\\Users\\last order\\Documents\\Tencent Files\\2495713984\\FileRecv\\[榎宫佑][台／简&精排] NO GAME NO LIFE游戏人生 01 听说游戏玩家兄妹要征服幻想世界.epub");

        InputStream in = null;

        try {
            //从输入流当中读取epub格式文件
            EpubReader reader = new EpubReader();
            in = new FileInputStream(f);
            Book book = reader.readEpub(in);
            //获取到书本的头部信息
            Metadata metadata = book.getMetadata();
            System.out.println("FirstTitle为："+metadata.getFirstTitle());
            //获取到书本的全部资源
            Resources resources = book.getResources();
            System.out.println("所有资源数量为："+resources.size());
            //获取所有的资源数据
            Collection<String> allHrefs = resources.getAllHrefs();
            List<Reader> result = new ArrayList<>();

            for (String href : allHrefs) {
                Resource resource = resources.getByHref(href);
                //data就是资源的内容数据，可能是css,html,图片等等
                byte[] data = resource.getData();
                // 获取到内容的类型  css,html,还是图片

                System.out.println("href ===== " + data.toString());

                Reader resultReader = new Reader();
                String mime = resource.getMediaType().toString();
                String content = new String(data, StandardCharsets.UTF_8);
                String html;
                switch (mime) {
                    case "image/jpeg":
                        html = "hello world";
                        break;
                    default:
                        html = content;
                        break;
                }
                resultReader.setContent(html);
                resultReader.setMime(mime);
                System.out.println(resource.getReader());

                result.add(
                    resultReader
                );
                MediaType mediaType = resource.getMediaType();
                System.out.println("=========" + mediaType + "========");
            }
            return  RestBean.success(result, "获取成功");
//            //获取到书本的内容资源
//            List<Resource> contents = book.getContents();
//            System.out.println("内容资源数量为："+contents.size());
//            //获取到书本的spine资源 线性排序
//            Spine spine = book.getSpine();
//            System.out.println("spine资源数量为："+spine.size());
//            //通过spine获取所有的数据
//            List<SpineReference> spineReferences = spine.getSpineReferences();
//            for (SpineReference spineReference : spineReferences) {
//                Resource resource = spineReference.getResource();
//                //data就是资源的内容数据，可能是css,html,图片等等
//                byte[] data = resource.getData();
//                // 获取到内容的类型  css,html,还是图片
//                MediaType mediaType = resource.getMediaType();
//            }
//            //获取到书本的目录资源
//            TableOfContents tableOfContents = book.getTableOfContents();
//            System.out.println("目录资源数量为："+tableOfContents.size());
//            //获取到目录对应的资源数据
//            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
//            for (TOCReference tocReference : tocReferences) {
//                Resource resource = tocReference.getResource();
//                //data就是资源的内容数据，可能是css,html,图片等等
//                String title = tocReference.getTitle();
//                System.out.println("title为 ======" + title);
//                byte[] data = resource.getData();
//                // 获取到内容的类型  css,html,还是图片
//                MediaType mediaType = resource.getMediaType();
//                if(tocReference.getChildren().size()>0){
//                    //获取子目录的内容
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //一定要关闭资源
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return RestBean.success(null, "获取成功");
    }

    public Object readEpub (File file) {
        // 例子 https://juejin.cn/post/7026282079356715016
        // 文档 http://www.siegmann.nl/epublib/example-programs/read-epub-file
        EpubReader reader = new EpubReader();
        System.out.println("====== 开始解析 ======");
        try {
            Book book = reader.readEpub(
                new FileInputStream(file)
            );
            System.out.println(book);
            //获取到书本的头部信息
            Metadata metadata = book.getMetadata();

            System.out.println("title为：" + metadata.getFirstTitle());

            Resources resources = book.getResources();
            System.out.println(resources);
//
//            Collection<Resource> all = resources.getAll();
//            System.out.println(all);
//
//            for (int i = 0; i < resources.size(); i++) {
//                Collection<Resource> item = resources.getData();
//            }
////            获取书内所有资源
//            Collection<String> allHrefs = resources.getAllHrefs();
//            for (String href : allHrefs) {
//                Resource resource = resources.getByHref(href);
//                //data就是资源的内容数据，可能是css,html,图片等等
//                byte[] data = resource.getData();
//                // 获取到内容的类型  css,html,还是图片

//            }
            List<Resource> content = book.getContents();
            Spine spine = book.getSpine();
            System.out.println(spine);
            Resource resource = spine.getResource(0);
            System.out.println(resource);
            System.out.println("=======" + content);
            byte[] data = resource.getData();

            String str = data.toString();
            System.out.println(str);
//            获取目录
            TableOfContents index = book.getTableOfContents();
            List<TOCReference> toc = index.getTocReferences();
            System.out.println(toc);

            return toc;

//            for (int i = 0; i < toc.size(); i++) {
//                TOCReference t = toc.get(i);
//                System.out.println("title" + t.getTitle());
//                System.out.println("getChildren" + t.getChildren());
//                System.out.println("getCompleteHref" + t.getCompleteHref());
//                System.out.println("getResource" + t.getResource());
//                System.out.println("getClass" + t.getClass());
//                System.out.println("getFragmentId" + t.getFragmentId());
//                System.out.println("getResourceId" + t.getResourceId());
//
//            }
//            for (TOCReference tocReference : toc) {
//                tocReference.getTitle();
//                Resource resource = tocReference.getResource();
//                //data就是资源的内容数据，可能是css,html,图片等等
//                byte[] data = resource.getData();
//                // 获取到内容的类型  css,html,还是图片

//                if(tocReference.getChildren().size()>0){
//                    //获取子目录的内容
//                }
//            }
        } catch (IOException e) {
            System.out.println("====== error =====");
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

}
