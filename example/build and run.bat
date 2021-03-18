@echo off
java -jar "jasmc.jar" "-asm" "-in<main.jasm>" "-out<main.bin>" "-log<PA>"
java -jar "jasmc.jar" "-exe" "-in<main.bin>" "-spd<1000>"
pause
