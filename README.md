# MiniTerminal 🗃️
## FINAL VERSION

> This project it's no longer mantained.

*English:*

Our intention was to create a terminal as optimized as possible for file management, this is an educational project and without any kind of profit. With the passage of time we decided to improve the code that the practice required us and that is why we added JLine3. It allowed us to do much more advanced things that will be detailed below.
________________________________________________________________________________________________________________________________________________________
*Spanish:*

Nuestra intención era crear una terminal lo mas optimizada posible para la gestión de ficheros, este es un proyecto educativo y sin nigun tipo de fines lucrativos. con el paso del tiempo nos propusimos mejorar el codigo que nos requeria la practica y por eso añadimos JLine3. Nos permitia hacer cosas mucho mas avanzadas que se detallaran a continuación.

### This project depends on [JLine3](https://github.com/jline/jline3) for its operation;

### How to use:
 1. Download: https://we.tl/t-AvF2oBm7IR
 2. Unzip the files and run: java -jar MiniTerminal.jar
 
### How to compile:
 1. Download and install [JLine3](https://github.com/jline/jline3)
 2. Clone the repository `git clone https://github.com/masterprog-cmd/MiniTerminalJava.git`
 3. Change working directory to the workspace.
 4. Execute `mvn compile && mvn -X exec:java -Dexec.mainClass=miniTerminal.MiniTerminal` to compile and execute directly.

  > You can also use IDES like eclipse to quickly compile your code.


Terminal functions:
- Autocomplete files, directories and commands
- Persistent history
- Multiline
- Autosuggestions

List of commands implemented:
```
pwd
cd [DIR]
ls [DIR]
ll [DIR]
mkdir <DIR>
touch <FILE>
rm <FILE>
mv <FILE1> <FILE2>
cat <FILE>
find <CRITERIA>
wget <URL> [FILE]
history [NUM]
nano [FILE]
clear
help [COMMAND] | ? [COMMAND]
exit | quit
```
