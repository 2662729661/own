/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.personage.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author lian
 */
@Controller
public class HollController {

        @RequestMapping(value = "/")
        public ModelAndView goHome(HttpServletResponse response) throws IOException {
                return new ModelAndView("page");
        }

        @RequestMapping(value = "/viewXsl")
        public ModelAndView viewXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 构建XML文件的绝对路径

                String xmlFile = "resources/a.xml";
                String contextPath = request.getServletContext().getRealPath("");
                String xmlFilePath = contextPath + File.separator + xmlFile;

                Source source = new StreamSource(new File(xmlFilePath));

                // 将XML源文件添加到模型中，以便XsltView能够检测
                ModelAndView model = new ModelAndView("XSLTViewXml");
                model.addObject("xmlSource", source);

                return model;

        }

        //获取xml-for
        @RequestMapping(value = "/viewXml")
        public ModelAndView viewXmlFor(HttpServletRequest request, HttpServletResponse response) throws Exception {
                //获取文档对象
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建请求方法
                HttpGet httpget = new HttpGet("https://redan-api.herokuapp.com/story/");

                //获取响应对象
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(httpget);
                try {
                        //获取响应体
                        HttpEntity entity1 = response1.getEntity();
                        if (entity1 != null) {
                                //设置编码字符集
                                String retSrc = EntityUtils.toString(entity1, "UTF-8");

                                //转换为json数据并且获取result中的数据
                                JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");
                                //获取根标签的值
                                JSONObject sx = new JSONObject(retSrc);
                                String string11 = sx.get("status").toString();
                                //创建根目录
                                Element document = doc.createElement("document");
                                document.setAttribute("status", string11);
                                doc.appendChild(document);

                                //获取所有数据
                                JSONObject jsonObject = storyList.getJSONObject(0);

                                //获取第一层所有key
                                Iterator<String> keys = jsonObject.keys();
                                //遍历第一层
                                for (int i = 0; i < jsonObject.length(); i++) {
                                        //获取key
                                        String next = keys.next();
                                        //获取值
                                        String string = jsonObject.get(next).toString();
                                        //创建第一层标签
                                        Element nextElement = doc.createElement(next);
                                        //判断值是否是json格式
                                        if (!(jsonObject.get(next) instanceof JSONObject) && !(jsonObject.get(next) instanceof JSONArray)) {
                                                System.out.println("1:" + next + "   " + string);
                                                //添加文本
                                                nextElement.setTextContent(string);
                                        }
                                        //添加标签
                                        document.appendChild(nextElement);
                                        //判断第二层是否是json数组
                                        if (jsonObject.get(next) instanceof JSONArray) {
                                                //获取json数组
                                                JSONArray jsonArray = jsonObject.getJSONArray(next);
                                                //遍历json数组
                                                for (int j = 0; j < jsonArray.length(); j++) {
                                                        //获取json对象
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                                        //获取json对象使用的key数组
                                                        Iterator<String> keys1 = jsonObject1.keys();
                                                        //遍历第二层json
                                                        for (int b = 0; b < jsonObject1.length(); b++) {
                                                                //获取key
                                                                String next1 = keys1.next();
                                                                //获取值
                                                                String toString = jsonObject1.get(next1).toString();
                                                                //创建第二层标签
                                                                Element next1Element = doc.createElement(next1);
                                                                //判断是否为json对象
                                                                if (!(jsonObject1.get(next1) instanceof JSONObject)) {
                                                                        System.out.println("2:" + next1 + "     " + toString);
                                                                        //添加文本
                                                                        next1Element.setTextContent(toString);
                                                                }
                                                                //添加节点
                                                                nextElement.appendChild(next1Element);
                                                                //判断第三层是否是json对象
                                                                if (jsonObject1.get(next1) instanceof JSONObject) {
                                                                        //获取json对象
                                                                        JSONObject jsonObject3 = jsonObject1.getJSONObject(next1);
                                                                        //获取key数组
                                                                        Iterator<String> keys2 = jsonObject3.keys();
                                                                        //遍历第三层
                                                                        for (int n = 0; n < jsonObject3.length(); n++) {
                                                                                //获取key
                                                                                String next2 = keys2.next();
                                                                                //创建第二层标签
                                                                                Element next2Element = doc.createElement(next2);
                                                                                //添加文本值
                                                                                next2Element.setTextContent(jsonObject3.getString(next2));
                                                                                //添加节点
                                                                                next1Element.appendChild(next2Element);
                                                                                System.out.println("4:" + next2 + "   " + jsonObject3.getString(next2));
                                                                        }
                                                                }

                                                        }
                                                }
                                                //判断第二层是否为json对象
                                        } else if (jsonObject.get(next) instanceof JSONObject) {
                                                //获取json对象
                                                JSONObject jsonObject2 = jsonObject.getJSONObject(next);
                                                //获取对象的key数组
                                                Iterator<String> keys1 = jsonObject2.keys();
                                                //遍历第二层
                                                for (int j = 0; j < jsonObject2.length(); j++) {
                                                        //获取key
                                                        String next1 = keys1.next();
                                                        //创建第二层标签
                                                        Element next1Element = doc.createElement(next1);
                                                        //添加文本值
                                                        next1Element.setTextContent(jsonObject2.getString(next1));
                                                        //添加节点
                                                        nextElement.appendChild(next1Element);
                                                        System.out.println("3:" + next1 + "   " + jsonObject2.getString(next1));
                                                }
                                        }

                                }

                        }

                } catch (Exception e) {
                        System.out.println(e);
                }
                //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

                Source source = new DOMSource(doc);

                // 将XML源文件添加到模型中，以便XsltView能够检测
                ModelAndView model = new ModelAndView("XSLTViewXml");
                model.addObject("xmlSource", source);

                return model;
        }

        //获取xml- iteration
        @RequestMapping(value = "/viewXmlIteration")
        public ModelAndView viewXmlIteration(HttpServletRequest request, HttpServletResponse response) throws Exception {
                //获取文档对象
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建请求方法
                HttpGet httpget = new HttpGet("https://redan-api.herokuapp.com/story/");

                //获取响应对象
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(httpget);
                try {
                        //获取响应体
                        HttpEntity entity1 = response1.getEntity();
                        if (entity1 != null) {
                                //设置编码字符集
                                String retSrc = EntityUtils.toString(entity1, "UTF-8");

                                //转换为json数据并且获取result中的数据
                                JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");
                                //获取根标签的值
                                JSONObject sx = new JSONObject(retSrc);
                                String string11 = sx.get("status").toString();
                                //创建根目录
                                Element document = doc.createElement("document");
                                document.setAttribute("status", string11);
                                doc.appendChild(document);

                                //获取所有数据
                                JSONObject jsonObject = storyList.getJSONObject(0);

                                //获取第一层所有key
                                Iterator<String> keys = jsonObject.keys();
                                //遍历第一层
                                while (keys.hasNext()) {
                                        //获取key
                                        String next = keys.next();

                                        //获取值
                                        String string = jsonObject.get(next).toString();
                                        //创建第一层标签
                                        Element nextElement = doc.createElement(next);
                                        //判断值是否是json格式
                                        if (!string.endsWith("]") && !string.endsWith("}")) {
                                                System.out.println("1:" + next + "   " + string);
                                                //添加文本
                                                nextElement.setTextContent(string);
                                        }
                                        //添加标签
                                        document.appendChild(nextElement);
                                        //判断第二层是否是json数组
                                        if (string.endsWith("]")) {
                                                //获取json数组
                                                JSONArray jsonArray = jsonObject.getJSONArray(next);
                                                //遍历json数组
                                                int i = 0;
                                                while (i < jsonArray.length()) {
                                                        //获取json对象
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        //获取json对象使用的key数组
                                                        Iterator<String> keys1 = jsonObject1.keys();
                                                        i++;
                                                        //遍历第二层json
                                                        while (keys1.hasNext()) {
                                                                //获取key
                                                                String next1 = keys1.next();
                                                                //获取值
                                                                String toString = jsonObject1.get(next1).toString();
                                                                //创建第二层标签
                                                                Element next1Element = doc.createElement(next1);
                                                                //判断是否为json对象
                                                                if (!toString.endsWith("}")) {
                                                                        System.out.println("2:" + next1 + "     " + toString);
                                                                        //添加文本
                                                                        next1Element.setTextContent(toString);
                                                                }
                                                                //添加节点
                                                                nextElement.appendChild(next1Element);
                                                                //判断第三层是否是json对象
                                                                if (toString.endsWith("}")) {
                                                                        //获取json对象
                                                                        JSONObject jsonObject3 = jsonObject1.getJSONObject(next1);
                                                                        //获取key数组
                                                                        Iterator<String> keys2 = jsonObject3.keys();
                                                                        //遍历第三层
                                                                        while (keys2.hasNext()) {
                                                                                //获取key
                                                                                String next2 = keys2.next();
                                                                                //创建第二层标签
                                                                                Element next2Element = doc.createElement(next2);
                                                                                //添加文本值
                                                                                next2Element.setTextContent(jsonObject3.getString(next2));
                                                                                //添加节点
                                                                                next1Element.appendChild(next2Element);
                                                                                System.out.println("4:" + next2 + "   " + jsonObject3.getString(next2));
                                                                        }
                                                                }

                                                        }
                                                }
                                                //判断第二层是否为json对象
                                        } else if (string.endsWith("}")) {
                                                //获取json对象
                                                JSONObject jsonObject2 = jsonObject.getJSONObject(next);
                                                //获取对象的key数组
                                                Iterator<String> keys1 = jsonObject2.keys();
                                                //遍历第二层
                                                while (keys1.hasNext()) {
                                                        //获取key
                                                        String next1 = keys1.next();
                                                        //创建第二层标签
                                                        Element next1Element = doc.createElement(next1);
                                                        //添加文本值
                                                        next1Element.setTextContent(jsonObject2.getString(next1));
                                                        //添加节点
                                                        nextElement.appendChild(next1Element);
                                                        System.out.println("3:" + next1 + "   " + jsonObject2.getString(next1));
                                                }
                                        }

                                }

                        }

                } catch (Exception e) {
                        System.out.println(e);
                }
                //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

                Source source = new DOMSource(doc);

                // 将XML源文件添加到模型中，以便XsltView能够检测
                ModelAndView model = new ModelAndView("XSLTViewXml");
                model.addObject("xmlSource", source);

                return model;
        }

        //获取xml- for 
        @RequestMapping(value = "/viewXmlFor")
        public void viewXmlFor2(HttpServletRequest request, HttpServletResponse response) throws Exception {
                //获取文档对象
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建请求方法
                HttpGet httpget = new HttpGet("https://redan-api.herokuapp.com/story/");

                //获取响应对象
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(httpget);
                try {
                        //获取响应体
                        HttpEntity entity1 = response1.getEntity();
                        if (entity1 != null) {
                                //设置编码字符集
                                String retSrc = EntityUtils.toString(entity1, "UTF-8");

                                //转换为json数据并且获取result中的数据
                                JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");
                                //获取根标签的值
                                JSONObject sx = new JSONObject(retSrc);
                                String string11 = sx.get("status").toString();
                                //创建根目录
                                Element document = doc.createElement("document");
                                document.setAttribute("status", string11);
                                doc.appendChild(document);

                                //获取所有数据
                                JSONObject jsonObject = storyList.getJSONObject(0);

                                //遍历第一层
                                for (Iterator<String> keys = jsonObject.keys(); keys.hasNext();) {

                                        //获取key
                                        String next = keys.next();
                                        //获取值
                                        String string = jsonObject.get(next).toString();
                                        //创建第一层标签
                                        Element nextElement = doc.createElement(next);
                                        //判断值是否是json格式
                                        if (!string.endsWith("]") && !string.endsWith("}")) {
                                                System.out.println("1:" + next + "   " + string);
                                                //添加文本
                                                nextElement.setTextContent(string);
                                        }
                                        //添加标签
                                        document.appendChild(nextElement);
                                        //判断第二层是否是json数组
                                        if (string.endsWith("]")) {
                                                //遍历json数组
                                                int i = 0;
                                                for (JSONArray jsonArray = jsonObject.getJSONArray(next); (i < jsonArray.length());) {
                                                        //获取json对象
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        i++;
                                                        //遍历第二层json
                                                        for (Iterator<String> keys1 = jsonObject1.keys(); keys1.hasNext();) {
                                                                //获取key
                                                                String next1 = keys1.next();
                                                                //获取值
                                                                String toString = jsonObject1.get(next1).toString();
                                                                //创建第二层标签
                                                                Element next1Element = doc.createElement(next1);
                                                                //判断是否为json对象
                                                                if (!toString.endsWith("}")) {
                                                                        System.out.println("2:" + next1 + "     " + toString);
                                                                        //添加文本
                                                                        next1Element.setTextContent(toString);
                                                                }
                                                                //添加节点
                                                                nextElement.appendChild(next1Element);
                                                                //判断第三层是否是json对象
                                                                if (toString.endsWith("}")) {
                                                                        //获取json对象
                                                                        JSONObject jsonObject3 = jsonObject1.getJSONObject(next1);
                                                                        //遍历第三层
                                                                        for (Iterator<String> keys2 = jsonObject3.keys(); keys2.hasNext();) {
                                                                                //获取key
                                                                                String next2 = keys2.next();
                                                                                //创建第二层标签
                                                                                Element next2Element = doc.createElement(next2);
                                                                                //添加文本值
                                                                                next2Element.setTextContent(jsonObject3.getString(next2));
                                                                                //添加节点
                                                                                next1Element.appendChild(next2Element);
                                                                                System.out.println("4:" + next2 + "   " + jsonObject3.getString(next2));
                                                                        }
                                                                }

                                                        }
                                                }
                                                //判断第二层是否为json对象
                                        } else if (string.endsWith("}")) {
                                                //获取json对象
                                                JSONObject jsonObject2 = jsonObject.getJSONObject(next);
                                                //遍历第二层
                                                for (Iterator<String> keys1 = jsonObject2.keys(); keys1.hasNext();) {
                                                        //获取key
                                                        String next1 = keys1.next();
                                                        //创建第二层标签
                                                        Element next1Element = doc.createElement(next1);
                                                        //添加文本值
                                                        next1Element.setTextContent(jsonObject2.getString(next1));
                                                        //添加节点
                                                        nextElement.appendChild(next1Element);
                                                        System.out.println("3:" + next1 + "   " + jsonObject2.getString(next1));
                                                }
                                        }

                                }

                        }

                } catch (Exception e) {
                        System.out.println(e);
                }
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

//                Source source = new DOMSource(doc);
//
//                // 将XML源文件添加到模型中，以便XsltView能够检测
//                ModelAndView model = new ModelAndView("XSLTViewXml");
//                model.addObject("xmlSource", source);
//
//                return model;
        }

        //无判断方式获取
        @RequestMapping(value = "/viewXmlS")
        public void viewXmlS(HttpServletRequest request, HttpServletResponse response) throws Exception {
                //获取文档对象
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建请求方法
                HttpGet httpget = new HttpGet("https://redan-api.herokuapp.com/story/");

                //获取响应对象
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(httpget);

                //获取响应体
                HttpEntity entity1 = response1.getEntity();
                if (entity1 != null) {
                        //设置编码字符集
                        String retSrc = EntityUtils.toString(entity1, "UTF-8");

                        //转换为json数据并且获取result中的数据
                        JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");
                        //获取根标签的值
                        JSONObject sx = new JSONObject(retSrc);
                        String string11 = sx.get("status").toString();

                        //创建根目录
                        Element document = doc.createElement("document");
                        document.setAttribute("status", string11);
                        doc.appendChild(document);

                        //获取所有数据
                        JSONObject jsonObject = storyList.getJSONObject(0);

                        //获取keys
                        Iterator<String> keys = jsonObject.keys();

                        //循环
                        for (int i = 0; i < jsonObject.length(); i++) {
                                //获取key
                                String next = keys.next();
                                //创建标签
                                Element createElement = doc.createElement(next);
                                //判断
                                if (!next.equals("comments") && !next.equals("author")) {
                                        //添加value
                                        createElement.setTextContent(jsonObject.get(next).toString());
                                }
                                //添加标签
                                document.appendChild(createElement);

                                //判断是不是Object
                                if (next.equals("author")) {
                                        //获取Object
                                        JSONObject jsonObject1 = jsonObject.getJSONObject(next);
                                        //获取keys
                                        Iterator<String> keys1 = jsonObject1.keys();
                                        //循环
                                        for (int j = 0; j < jsonObject1.length(); j++) {
                                                //获取key
                                                String next1 = keys1.next();
                                                //创建标签
                                                Element createElement1 = doc.createElement(next1);
                                                //添加文本
                                                createElement1.setTextContent(jsonObject1.get(next1).toString());
                                                //添加标签
                                                createElement.appendChild(createElement1);
                                        }
                                }

                                //判断是不是Array
                                if (next.equals("comments")) {
                                        //获取Array
                                        JSONArray jsonArray = jsonObject.getJSONArray(next);
                                        //循环
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                                //获取对象
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                                //获取keys
                                                Iterator<String> keys1 = jsonObject1.keys();
                                                //循环
                                                for (int k = 0; k < jsonObject1.length(); k++) {
                                                        //获取key
                                                        String next1 = keys1.next();
                                                        //创建标签
                                                        Element createElement1 = doc.createElement(next1);
                                                        //判断
                                                        if (!next1.equals("who")) {
                                                                //添加文本
                                                                createElement1.setTextContent(jsonObject1.get(next1).toString());
                                                        }
                                                        //添加标签
                                                        createElement.appendChild(createElement1);
                                                        //判断
                                                        if (next1.equals("who")) {
                                                                //获取Object
                                                                JSONObject jsonObject2 = jsonObject1.getJSONObject(next1);
                                                                //获取keys
                                                                Iterator<String> keys2 = jsonObject2.keys();
                                                                //循环
                                                                for (int z = 0; z < jsonObject2.length(); z++) {
                                                                        //获取key
                                                                        String next2 = keys2.next();
                                                                        //创建标签
                                                                        Element createElement2 = doc.createElement(next2);
                                                                        //添加文本
                                                                        createElement2.setTextContent(jsonObject2.get(next2).toString());
                                                                        //添加标签
                                                                        createElement1.appendChild(createElement2);
                                                                }

                                                        }
                                                }
                                        }
                                }
                        }

                }
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
        }

        @RequestMapping(value = "/vi")
        public void view(HttpServletRequest request, HttpServletResponse response) throws Exception {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建的根元素
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
                //创建客户端

                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
                //返回字符串实体

                HttpEntity entity1 = response1.getEntity();
                if (entity1 != null) {
                        //给返回的实体设置编码
                        String retSrc = EntityUtils.toString(entity1, "UTF-8");
                        //获得最外层的元素的是个数组
                        JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");

                        // 那么遍历里面的元素取出所有的元素
                        for (int i = 0; i < storyList.length(); i++) {
                                //拿到json 每个数组元素判断json数组判断元素是否对象还是数组
                                JSONObject jsonObject = storyList.getJSONObject(i);

                                Iterator<String> next1 = jsonObject.keys();

                                while (next1.hasNext()) {
                                        String next2 = next1.next();
                                        //文本的情况下
                                        Element documentNext2 = doc.createElement(next2);
                                        if (!(next2.equals("author") || next2.equals("comments"))) {
                                                documentNext2.setTextContent(next2);
                                                System.out.println("是文本的情况" + next2);
                                        }
                                        documentElement.appendChild(documentNext2);
                                        //在comments数组中
                                        if (next2.equals("comments")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray(next2);
                                                //创建标签节点

                                                for (int j = 0; j < jsonArray.length(); j++) {

                                                        JSONObject jsonObject3 = jsonArray.getJSONObject(j);
                                                        Iterator<String> next3 = jsonObject3.keys();
                                                        while (next3.hasNext()) {
                                                                String next4 = next3.next();
                                                                Element createElement = doc.createElement(next4);
                                                                if (!next4.equals("who")) {
                                                                        String vaString = jsonObject3.getString(next4);
                                                                        createElement.setTextContent(vaString);
                                                                        System.out.println("不是who就是文本" + vaString);

                                                                }
                                                                if (next4.equals("who")) {
                                                                        JSONObject jsonObject4 = jsonObject3.getJSONObject(next4);
                                                                        Iterator<String> next5 = jsonObject4.keys();
                                                                        while (next5.hasNext()) {
                                                                                String next6 = next5.next();
                                                                                String vaString = jsonObject4.getString(next6);
                                                                                Element documentNext6 = doc.createElement(next6);
                                                                                documentNext6.setTextContent(vaString);
                                                                                System.out.println("who的值***************************" + vaString);
                                                                                createElement.appendChild(documentNext6);
                                                                        }
                                                                }

                                                                documentNext2.appendChild(createElement);
                                                        }

                                                }

                                        }
                                        if (next2.equals("author")) {
                                                JSONObject jsonObject5 = jsonObject.getJSONObject(next2);
                                                Iterator<String> next7 = jsonObject5.keys();
                                                while (next7.hasNext()) {
                                                        String next8 = next7.next();
                                                        String vaString2 = jsonObject5.getString(next8);
                                                        Element documentNext8 = doc.createElement(next8);
                                                        documentNext8.setTextContent(vaString2);
                                                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + vaString2);
                                                        documentNext2.appendChild(documentNext8);
                                                }

                                        }

                                }

                        }

                }
//更新到本项目
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
                System.out.println("结束");
        }

}
