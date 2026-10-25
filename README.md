# Simulador-de-automato
Compreender a utilização da memória em pilha para o reconhecimento de linguagens que não podem ser adequadamente tratadas apenas por autômatos finitos, a fim de construir um analisador léxico básico.

O simulador reconhece parênteses balanceados. O analisador léxico separa `(` e `)`, e o autômato com pilha verifica o balanceamento.

## Como executar

```bat
build.bat
```

Para um arquivo só:

```bat
build.bat tests\validas.txt
```

