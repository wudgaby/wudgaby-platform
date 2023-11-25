```
java: You aren’t using a compiler supported by lombok, so lombok will not work and has been disabled.
Your processor is: com.sun.proxy.$Proxy26
Lombok supports: OpenJDK javac, ECJ
```

### 解决办法：
```
settings中搜索Compiler，在Idea的全局配置Compiler中添加如下配置：
-Djps.track.ap.dependencies=false
```
---
### Map<object, list<object>>使用不加判断处理的处理方法
```java
  public static void test001() {
        HashMap<Integer, List<Integer>> objectObjectHashMap =
                Maps.newHashMap();
        for (int i = 0; i < 10; i++) {
            //不使用判断直接操作 也就是省略了 最后边的代码
            objectObjectHashMap.computeIfAbsent(i%2, ArrayList::new).add(i);
        }
        //也可以使用这个 google的这个
        ListMultimap<Integer, Integer> build = MultimapBuilder.hashKeys()
                                                            .arrayListValues()
                                                            .build();
        build.put(1,100000);
        objectObjectHashMap.forEach(build::putAll);
        System.out.println(build.get(1));

        //以上代码可以省略此处的判断
        if(objectObjectHashMap.containsKey(1)) {
            objectObjectHashMap.get(1).add(1);
        } else {
            ArrayList<Integer> objects = new ArrayList<>();
            objects.add(1);
            objectObjectHashMap.put(1, objects);
        }
        //end
    }
```