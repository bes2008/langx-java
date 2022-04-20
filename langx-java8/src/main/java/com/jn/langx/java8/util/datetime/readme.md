
# LocalTime
    只用于表示时间，HH:mm:ss 
    它不带日期、时区等信息
    另外它支持到纳秒级。
    但因为它是Local的，即它代表的是本地时间，也就是表示的其实是经过 zone offset 处理后的时间，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的时间。
    它里面存储了当地  hour, minute, sencond, nanos
    
# LocalDate
    只用于表示日历， yyyy-MM-dd    
    它不带时间、时区等信息
    但因为它是Local的，即它代表的是本地日期，也就是表示的其实是经过 zone offset 处理后的日期，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的日期。
    它里面存储了当地  year, month, dayOfMonth
    
# LocalDateTime
    用于表示 日期、时间，但它不带 时区信息
    也可以理解成： LocalDateTime = LocalDate + LocalTime
    所以它是 java.util.Date 的替代品，并且支持纳秒，java.util.Date 是不支持纳秒的
    但因为它是Local的，即它代表的是本地时间，也就是表示的其实是经过 zone offset 处理后的时间，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的时间。
    如果想要根据它来获取UTC时间，需要调用 toEpochSeconds(ZoneOffest)， 因为它不保留offset，所以 要把offset 作为参数传给它。
    


# ZonedDateTime
    用于表示 日期、时间，但它带 时区信息
    也可以理解成：ZonedDateTime = LocalDateTime + ZoneId= LocalDate + LocalTime + ZoneId
    所以它是 java.util.Calendar 的替代品

# OffsetDateTime
    用于表示 日期、时间，但它带 时区信息
    也可以理解成：OffsetDateTime = LocalDateTime + ZoneOffset= LocalDate + LocalTime + ZoneOffset
    相比 LocalDateTime, 它保留了与 UTC时间的 offset
      
# TimeZone vs ZoneId, ZoneOffset
    TimeZone = ZoneId + ZoneOffset
    ZoneId 只有 id, 是个字符串，例如  Europe/Paris.
    ZoneOffset 只有 offset （一个数字，或者用 + - 符号表示的 一个人为刻度的偏移量，内部仍旧会被 计算为偏离量，并且是以  秒 s 为标准）

    
# Notes
1. 不论是java.util.Calendar 还是 Java 8 的API，都是不带 Locale 信息的，只有在 format, parse 时，才会带有 Locale信息，为的是 字符串时间 对当地人友好
2. 在Java8 下推荐直接使用 LocalDateTime，而不是Date的原因：
   1. 在为了获取 year, month, dayOfMonth, hour, minute, second, nanos 的场景下可以直接使用 LocalDateTime，因为对象在创建时，就已经计算到当地时间的这些字段了。 从 Date中获取这些字段，还要经过一番计算，拿到的值还未必是你期望的，另外需要做处理。
3. 



    
    












