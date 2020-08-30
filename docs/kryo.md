# Kryo 序列化

* 支持 eclipse-collections 的集合类型的序列化/反序列化

* Kryo [池化封装](https://github.com/EsotericSoftware/kryo#pooling)
```java
class Demo {
    KryoHelper kryoPool = new KryoHelper(kryo -> {
        kryo.register(User.class);
    });

    void demo() {
        byte[] bytes = kryoPool.writeClassAndObject(user);
        User user = kryoPool.readClassAndObject(bytes);
    }
}
```  