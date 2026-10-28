# Лаборатори №8 — Mockito mock объектууд

- Хичээл: F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест
- Оюутан: Л. Ирмүүн
- Оюутны код: B232270021
- Үйлдлийн систем: Ubuntu Linux
- IDE: Visual Studio Code

## Зорилго

CourseRegistrationService классын хамаарлуудыг Mockito
mock-оор орлуулж, бүртгэлийн бизнес логикийг тусгаарласан
нэгжийн тестээр шалгах.

## Ажлын орчин

`java -version` командын гаралт:

```text
openjdk version "21.0.12.1" 2026-08-18
OpenJDK Runtime Environment (build 21.0.12.1+1-1-24.04.4-Ubuntu)
OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-24.04.4-Ubuntu, mixed mode, sharing)
```

`mvn -version` командын гаралт:

```text
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 21.0.12.1, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "7.0.0-38-generic", arch: "amd64", family: "unix"
```

## Төслийн эх сурвалж

pom.xml болон src/main доторх дөрвөн Java файлыг
багшийн Lab08 starter төслөөс авсан.
StudentRepository болон EmailSender интерфейсүүдийг
тестэд Mockito-гоор орлуулах тул бодит өгөгдлийн сан,
SMTP серверийн хэрэгжүүлэлт шаардлагагүй.

### maven build алдааны засвар

Starter-ийн pom.xml-д maven-compiler-plugin 3.13.0-ийг
тодорхой зааж, Java release 17 тохируулсан.
Ингэснээр хуучин compiler plugin-ийн Java 5 тохиргооноос
үүссэн хөрвүүлэлтийн алдааг зассан.

## Тестийн төлөвлөгөө

Тестлэгдэх нэгж нь CourseRegistrationService.register() метод.
StudentRepository болон EmailSender-ийг Mockito mock-оор
орлуулна. Service болон Student өгөгдлийг бодит объект
хэлбэрээр ашиглана.

| ID | Нөхцөл | Хүлээгдэх үр дүн | Харилцан үйлчлэлийн шалгалт |
|---|---|---|---|
| A1 | Идэвхтэй оюутан, байгаа хичээл, урьдач нөхцөл хангагдсан | OK | Бүртгэл хадгалсан, и-мэйл илгээсэн |
| A2 | Оюутан олдохгүй | ERROR_NO_STUDENT | Хадгалаагүй, и-мэйл илгээгээгүй |
| A3 | Оюутан идэвхгүй | ERROR_INACTIVE_STUDENT | И-мэйлтэй огт харьцаагүй |
| A4 | Урьдач хичээл дутуу | ERROR_PREREQUISITES | Бүртгэл хадгалаагүй |
| A5 | И-мэйл илгээгч RuntimeException шиднэ | OK | Бүртгэл хадгалсан |
| A6 | studentId null болон courseId null-ийг тус тус өгөх | IllegalArgumentException | Хоёр хамааралтай огт харьцаагүй |
| A7 | Амжилттай бүртгэлийн и-мэйл | Body-д хичээлийн код байна | ArgumentCaptor-оор body барьж авах |
| A8 | Байхгүй оюутан, идэвхгүй оюутан, байхгүй хичээл гэсэн гурван мөр | Тухайн ERROR утга | Алдаатай бүртгэл хадгалаагүй, и-мэйл илгээгээгүй |

Төлөвлөсөн бүтэц нь 7 энгийн тестийн метод болон
3 мөртэй нэг parameterized метод буюу нийт 8 метод,
10 тестийн гүйцэтгэл байна. Бодит гүйцэтгэлийн тоог
тестүүдийг дуусгасны дараа Maven-ийн гаралтаар баталгаажуулна.

## A8-ийн lenient тохиргоо

A8-ийн нийтлэг setup-д courseExists() false буцаахаар
тохируулсан боловч байхгүй болон идэвхгүй оюутны мөрүүд
энэ дуудлагад хүрэхээс өмнө дуусдаг.
Иймээс зөвхөн тухайн stub-д lenient() хэрэглэж,
ашиглагдаагүй stub-ийн strict шалгалтаас чөлөөлсөн.

## Амжилттай тестийн үр дүн

| Үзүүлэлт | Үр дүн |
|---|---:|
| Тестийн метод | 8 |
| Tests run | 10 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| Maven exit code | 0 |

Долоон энгийн тест болон A8-ийн гурван parameterized
гүйцэтгэл нийлж нийт 10 тест ажилласан.
Maven-ийн эцсийн үр дүн BUILD SUCCESS болсон.

[Тестийн бүтэн гаралт](results/mvn-test.txt)