# Marckdown converter to HTML

Виконала: Фесун Анна ІМ-21

## Короткий опис застосунку

Застосунок розроблений на мові програмування Java для перетворення текстового файлу із розміткою Markdown в фрагмент HTML-розмітки або в формат ANSI. 
Застосунок приймає на вхід файл з розширенням .txt та виводить або в стандартний вивід з викорстанням ANSI Escape Codes, або за наявності аргумента з
вихідним файлом --out /path/to/output.html — у вихідний файл. Додано можливість вибору формату виводу, реалізовано це через прапорець командного рядка 
--format=<html/ansi> У разі, якщо розмітка у вхідному файлі є некоректною (помилки елементів 
розмітки враховані відповідно до методичних вказівок в лабораторній роботі №1), застосунок виводить помилку у стандартну помилку (stderr)
та завершується з не-нульовим кодом виходу. Застосунок має модульний підхід до розв'язання задачі. 


## Інструкція, як зібрати та запустити проєкт

1. Якщо у вас не встановлений JDK, встановіть його за посиланням: 
<a name="jdk" href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">Завантажити JDK</a>
2. Завантажте репозиторій на локальний комп'ютер.
3. Запустіть термінал в кореневій папці репозиторію.
4. Введіть для запуску застосонку команду "./gradlew run" в термінал.
5. За потреби можна змінити аргументи для запуску в файлі build.gradle. 
 
## Інструкція до використання проєкту

Щоб використати проєкт, виконайте інструкції описані вище. Після введення команди "./gradlew run", запускається сам застосунок
із вхідними аргументами (за замовчуванням args = ['./src/main/resources/test.txt', '--out', './src/main/resources/output.html']).
Відповідно застосунок за замовчуванням бере файл test.txt з папки ресурсів та результат перетворень заносить до файлу output.html 
у цій же папці. Ви можете змінити текст вхідного файлу або змінити самі аргументи в файлі build.gradle, якщо вам це необхідно.
Також якщо в файлі build.gradle прописати лише один аргумент (шлях до вхідного файлу), то перетворений текст буде виведений в термінал
з викорстанням ANSI, приклад такої команди: args './src/main/resources/test.txt'

## Інструкція, як запустити тести

В кореневій папці проєкту в терміналі запустити команду "./gradlew test" команда запустить усі тести наявні в проєкті. 
Також можна запустити тести у вкладці run з використанням команди "gradle test".

## Вказання на revert-commit

SHA: 528167812f668c966ba68175c9d321df3a9287e3

<a name="revert" href="https://github.com/AnyaFesun/markdownToHTML/commit/528167812f668c966ba68175c9d321df3a9287e3">Revert-commit</a>

 ## Посилання на коміт, на якому впали тести

SHA: c31c15007166623fd13eb9573a53187048f7d655

<a name="drop" href="https://github.com/AnyaFesun/markdownToHTML/commit/c31c15007166623fd13eb9573a53187048f7d655">Commit where the tests failed</a>

 ## Висновки

Написання unit-тестів для мого коду абсолютно себе виправдало і стало невід'ємною частиною моєї розробки. 
Для себе я виділила декілька переваг тестування, а саме: покращена надійність коду, спрощення рефакторингу,
краща документація, швидша розробка. Отже, я можу з впевненістю сказати, що час, витрачений на написання unit-тестів,
був дуже корисним інвестуванням. Це дозволило мені створювати більш якісний і надійний код, який легше підтримувати і масштабувати. 
