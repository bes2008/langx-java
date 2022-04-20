
# LocalTime
    只用于表示时间，hh:mm:ss 
    它不带日期、时区等信息
    另外它支持到纳秒级。
    但因为它是Local的，即它代表的是本地时间，也就是表示的其实是经过 zone offset 处理后的时间，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的时间。
    
# LocalDate
    只用于表示日历， yyyy-MM-dd    
    它不带时间、时区等信息
    但因为它是Local的，即它代表的是本地日期，也就是表示的其实是经过 zone offset 处理后的日期，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的日期。
    
# LocalDateTime
    用于表示 日期、时间，但它不带 时区信息
    也可以理解成： LocalDateTime = LocalDate + LocalTime
    所以它是 java.util.Date 的替代品，并且支持纳秒，java.util.Date 是不支持纳秒的
    但因为它是Local的，即它代表的是本地时间，也就是表示的其实是经过 zone offset 处理后的时间，
    但因为处理完了后，又不保留 zone offset信息，所以说它是不带 zone的时间。
    
    如果想要根据它来获取UTC时间，需要调用 toEpochSeconds(Zone)

# ZonedDateTime
    用于表示 日期、时间，但它带 时区信息
    也可以理解成：ZonedDateTime = LocalDateTime + ZoneId = LocalDate + LocalTime + ZoneId
    所以它是 java.util.Calendar 的替代品

    

    
    












