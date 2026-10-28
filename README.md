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

Starter-ийн pom.xml-д maven-compiler-plugin 3.13.0-ийг
тодорхой зааж, Java release 17 тохируулсан.
Ингэснээр хуучин compiler plugin-ийн Java 5 тохиргооноос
үүссэн хөрвүүлэлтийн алдааг зассан.