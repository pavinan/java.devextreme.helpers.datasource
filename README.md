# DevExtreme helpers remote datasource.
Ported from [DevExtreme.Helpers.DataSource](https://github.com/pavinan/DevExtreme.Helpers.DataSource) written in C#

```java
WhereClauseBuilder builder = new WhereClauseBuilder();
String json = "[\"CustomerID\",\"=\",\"ALFKI\"]";
JsonArray jArray = (JsonArray) (new JsonParser().parse(json));
String sqlWhereClause = builder.buildFor(jArray);
```

Outputs:
```
CustomerID = "ALFKI"
```

In sql you can write
```sql
Exec('Select * from customers where ' + @sqlWhereClause);
```

There is an overloaded version of `WhereClauseBuilder` you can provide your own implentation `SQLColumnProvider` if you are using table aliases you can customize there.
