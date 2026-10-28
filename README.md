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

## Хэсэг A — Хэрэгжүүлсэн тестүүд

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

Хэрэгжүүлсэн бүтэц нь 7 энгийн тестийн метод болон 3 мөртэй нэг parameterized метод буюу нийт 8 метод, 10 тестийн гүйцэтгэлтэй. Энэ тоо results/mvn-test.txt-ийн сүүлийн Tests run: 10 хураангуйтай таарна. A6-ийн хоёр null оролтыг нэг @Test метод дотор тус тус шалгасан тул хоёр тусдаа гүйцэтгэл гэж тоолоогүй.

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

## Хэсэг B — Мини мутацийн туршилт

Мутант бүрийг тусад нь үүсгэж, тестийн кодыг өөрчлөхгүйгээр ажиллуулсан. Дараагийн мутацийн өмнө өмнөх өөрчлөлтийг сэргээсэн. Мутаци бүрийн бүтэн гаралтыг тусдаа файлд хадгалж, бүх өөрчлөлтийг сэргээсний дараа эцсийн PASS гаралтыг дахин үүсгэсэн.

| Мутант | Түр өөрчилсөн зүйл | Унасан тест | Tests run | Failures | Errors | Skipped | Exit code | Build |
|---|---|---|---:|---:|---:|---:|---:|---|
| 1 | Идэвхгүй оюутны шалгалтыг комментлосон | A3, A8-ийн INACTIVE мөр | 10 | 2 | 0 | 0 | 1 | BUILD FAILURE |
| 2 | saveRegistration(...) дуудалтыг комментлосон | A1, A5 | 10 | 2 | 0 | 0 | 1 | BUILD FAILURE |
| 3 | Эхний if-ээс courseId == null хэсгийг хассан | A6 | 10 | 1 | 0 | 0 | 1 | BUILD FAILURE |
| Сэргээсэн эх код | Гурван өөрчлөлтийг буцаасан | Байхгүй | 10 | 0 | 0 | 0 | 0 | BUILD SUCCESS |

### Мутант 1 — Идэвхгүй оюутны шалгалт

[Бүтэн гаралт](results/mvn-test-mutant-1.txt)

Унасан тестүүд:

- `register_inactiveStudent_returnsErrorWithoutEmailInteraction` — A3.
- `register_invalidScenario_returnsExpectedError(String, String)[2]` — A8-ийн INACTIVE мөр.

Хоёуланд нь дараах assertion-ийн зөрүү гарсан:

```text
expected: <ERROR_INACTIVE_STUDENT> but was: <ERROR_NO_COURSE>
```

Идэвхтэй эсэхийг шалгахаа больсноор код хичээлийн шалгалт руу орсон. A3-д courseExists()-ийн default хариу false, A8-д тохируулсан хариу false байсан тул ERROR_NO_COURSE буцсан. Хүлээгдэх зөв алдааны утгыг шалгасан assertion-ууд мутацийг илрүүлсэн.

### Мутант 2 — Бүртгэл хадгалах дуудлага

[Бүтэн гаралт](results/mvn-test-mutant-2.txt)

Унасан тестүүд:

- `register_activeStudent_savesRegistrationAndSendsEmail` — A1.
- `register_emailSenderFails_returnsOkAndSavesRegistration` — A5.

Хоёуланд нь хадгалалтын verify унасан:

```text
Wanted but not invoked:
repository.saveRegistration(
    "S100",
    "F.CSA313"
);
```

Service OK буцаасан ч хадгалалт хийгдээгүйг verify илрүүлсэн. Зөвхөн буцаах утгыг шалгасан бол энэ өөрчлөлтийг тухайн assertion барихгүй байв.

### Мутант 3 — courseId-ийн null шалгалт

[Бүтэн гаралт](results/mvn-test-mutant-3.txt)

Унасан тест:

- `register_eitherIdIsNull_throwsWithoutDependencyInteractions` — A6.

```text
Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.
```

A6-д studentId=null болон courseId=null-ийг анхнаас нь тус тус шалгасан тул энэ мутацийг барихын тулд тест нэмж засах шаардлага гараагүй. courseId null үед эхний шалгалт алгасагдаж, mock repository-ийн findStudent() default null буцааснаар ERROR_NO_STUDENT үр дүнтэй дууссан; харин тест IllegalArgumentException хүлээж байсан.

### Мутацийн үр дүнгийн тайлбар

Сонгосон гурван мутаци бүгд assertion эсвэл verify-ийн Failures-аар илэрсэн; эдгээр гаралтад Errors болон UnnecessaryStubbingException бүртгэгдээгүй. Энэ нь зөв үр дүнгийн assertion, шаардлагатай үйлдлийн verify, null оролтын exception шалгалт өөр өөр төрлийн эвдрэлийг илрүүлж байгааг харуулна. Зөвхөн эдгээр гурван мутацийг барьсан нь бүх боломжит согогийг илрүүлнэ гэсэн баталгаа биш. Эцсийн PASS нь Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, BUILD SUCCESS, Maven exit code: 0 байна.

## Тестийн бүтэц ба ашигласан Mockito боломжууд

- `@ExtendWith(MockitoExtension.class)` нь mock-уудын JUnit lifecycle болон strict stubs шалгалтыг идэвхжүүлнэ.
- `@Mock`-оор хоёр гадаад хамаарлыг үүсгэж, `@BeforeEach`-д бодит service-ийг constructor-оор тэдгээртэй холбоно.
- Тестүүд Arrange–Act–Assert бүтэцтэй, тест бүр өөрийн Student өгөгдөл болон шаардлагатай stub-уудыг бэлтгэдэг.
- `when(...).thenReturn(...)` нь repository-ийн хариуг удирдана.
- `verify`, `never`, `verifyNoInteractions` нь хийх ёстой болон хийх ёсгүй харилцан үйлчлэлийг шалгана.
- `doThrow` нь void send() методын RuntimeException-ийг дуурайлгана.
- `ArgumentCaptor` нь service-ийн үүсгэсэн бодит и-мэйлийн body-г барьж, хичээлийн код орсныг шалгана.
- Нэг дуудалтад matcher хэрэглэвэл бүх аргументыг `eq`, `anyString`, `contains` эсвэл captor matcher-аар өгсөн.

## Ажиллуулах ба нотолгоог дахин үүсгэх

pom.xml байгаа репозиторийн үндсэн хавтсанд:

```bash
mvn test
```

Бүтэн PASS гаралт болон Maven-ийн exit code-ыг хадгалах Bash команд:

```bash
mkdir -p results
mvn test 2>&1 | tee results/mvn-test.txt
run_exit=${PIPESTATUS[0]}
printf 'Maven exit code: %s\n' "$run_exit" | tee -a results/mvn-test.txt
```

PIPESTATUS-ийг pipeline-ийн яг дараа авна. tee-ийн дараах дан $? нь Maven-ийн бус tee-ийн exit code болж болох тул ашиглаагүй. Мутацийн үед гаралтын нэрийг mvn-test-mutant-1.txt, mvn-test-mutant-2.txt, mvn-test-mutant-3.txt гэж тус тус өгсөн. Эцсийн зөв кодоор mutant файлуудыг дарж бичихгүй.

JDK 21 дээр гарсан Byte Buddy dynamic agent болон class sharing warning-ууд бүтэн гаралтад хадгалагдсан. Тэдгээр нь энэ ажиллуулалтын Failures/Errors биш бөгөөд эцсийн build амжилттай болсон.

## Дүгнэлт

Энэ лабораторийн ажлаар CourseRegistrationService-ийн логикийг өгөгдлийн сан болон и-мэйл үйлчилгээний бодит хэрэгжүүлэлтгүйгээр тусгаарлан тестэлсэн. Mockito mock нь бэлдсэн хариу буцаахаас гадна дуудлагыг verify хийх боломжтой бол гараар бичсэн fake нь интерфейсийн хялбарчилсан ажилладаг хэрэгжүүлэлтийг өөрсдөө боловсруулахыг шаарддаг. Repository болон EmailSender зэрэг гадаад хамаарлыг орлуулсан боловч тестлэгдэх service болон Student өгөгдлийг mock хийгээгүй. Нийт найман тестийн методын арван гүйцэтгэл эцсийн зөв код дээр амжилттай болсон. Идэвхгүй оюутны шалгалтыг хассан мутацийг A3 болон A8-ийн INACTIVE мөр, хадгалалтын дуудлагыг хассан мутацийг A1 болон A5 илрүүлсэн. courseId-ийн null шалгалтыг хасахад A6 унасан нь хоёр аргументын null нөхцөлийг тус тус шалгахын ач холбогдлыг харуулсан. Verify-ийг хэрэгжүүлэлтийн бүх дотоод дуудлагад хэтрүүлэн хэрэглэх нь зан төлөв өөрчлөгдөөгүй refactor хийхэд ч тестийг хэврэг болгох эрсдэлтэй. Иймээс шаардлагаар чухал болсон хадгалалт, мэдэгдлийн үйлдлийг шалгаж, мутацийн нотолгоогоор assertion болон verify-ийн үр нөлөөг үнэлсэн.

## AI ашигласан тухай

ChatGPT-ийг даалгавар, лекцийг тайлбарлуулах, Mockito тестийн кодын ноорог боловсруулах, build тохиргооны алдааг оношлох болон тайлангийн найруулгад ашигласан. Тестүүдийг өөрийн Ubuntu орчинд ажиллуулж, гурван мутацийг тус тус туршин бүтэн Maven гаралтуудыг цуглуулсан. Тайлангийн тестийн тоо, унасан тестүүд, exit code-ыг эдгээр бодит гаралттай тулгасан.
