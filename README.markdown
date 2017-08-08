RWX XML-RPC Object-Binding API
=======================================

RWX is a set of APIs for dealing with XML-RPC interactions in Java. It offers
an annotation-based object-binding API for interacting via Java objects.

It uses Java Annotation Processor API to generate parser / renderer Java source files
according to RWX annotations. The top level RWXMapper uses those generated classes under the hook.

You will need 4 steps to use RWX:

1. Create model classes according to your XML-RPC req/response format.
2. Annotate model classes by RWX annotations.
3. Generate sources form annotated classes (an pom example is provided in rwx-test module)
4. Call RWXMapper.render/parse methods to convert between XML-RPC req/resp strings and java objects.

The first two steps could be simplified by binding a field to java.lang.Object if you
do not know the exact Java type. In such cases, RWX will bind a List/Map based data structure to the field and you
can always interpret it in your program.

