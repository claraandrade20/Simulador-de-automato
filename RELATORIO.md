# Parênteses balanceados

## Definição formal da linguagem

Alfabeto: Σ = { `(`, `)` }.

L é a linguagem dos parênteses balanceados, gerada por:

```
S → ( S ) S | ε
```

Estão em L: `ε`, `()`, `(())`, `(()())`.
Estão fora de L: `(`, `)`, `)(`, `(a)`.

## Descrição do autômato com pilha

M = (Q, Σ, Γ, δ, q0, Z0, F)

- Q = { q0, q_aceita, q_rejeita }
- Σ = { `(`, `)` }
- Γ = { Z0, X }
- q0 é o estado inicial
- Z0 é o fundo da pilha
- F = { q_aceita }

O analisador léxico separa os símbolos em `ABRE`, `FECHA` ou `ERRO`. Espaços são descartados. O autômato só balanceia cadeias feitas de `(` e `)`. Cada `(` empilha um marcador X. Cada `)` remove um X. A cadeia é aceita quando a entrada termina e a pilha voltou a Z0.

Na saída, a pilha aparece entre colchetes, do fundo até o topo: `[Z0 X]` significa que X está no topo.

## Estados, transições e operações da pilha

| Transição | Operação |
| --- | --- |
| δ(q0, `(`, Z0) = (q0, X Z0) | empilha X |
| δ(q0, `(`, X) = (q0, X X) | empilha X |
| δ(q0, `)`, X) = (q0, ε) | desempilha X |

`)` com topo Z0 não tem transição: o autômato vai a q_rejeita.
Símbolo fora de Σ também vai a q_rejeita.
No fim da entrada, pilha Z0 leva a q_aceita. Se ainda houver X, vai a q_rejeita.

## Implementação

O projeto fica separado em pastas:

- `src/lexer/parenteses.flex` gera o analisador léxico
- `src/Token.java` guarda o token
- `src/AutomatoPilha.java` executa a pilha e monta o rastro
- `src/Main.java` lê um arquivo de testes
- `tests/validas.txt` e `tests/invalidas.txt` têm as cadeias
- `lib` guarda o JFlex, `gen` o código gerado e `bin` as classes

Para rodar os dois arquivos de teste:

```
build.bat
```

Para um arquivo só:

```
build.bat tests\validas.txt
```

## Exemplos de execução

Cadeia aceita `(()())`:

```
---- (()()) ----
Passo  Simbolo  Topo   Operacao             Estado       Pilha
1      (        Z0     EMPILHA X            q0           [Z0 X]
2      (        X      EMPILHA X            q0           [Z0 X X]
3      )        X      DESEMPILHA X         q0           [Z0 X]
4      (        X      EMPILHA X            q0           [Z0 X X]
5      )        X      DESEMPILHA X         q0           [Z0 X]
6      )        X      DESEMPILHA X         q0           [Z0]
7      fim      Z0     aceita               q_aceita     [Z0]
Resultado: ACEITA
```

Cadeia rejeitada `(a)`:

```
---- (a) ----
Passo  Simbolo  Topo   Operacao             Estado       Pilha
1      (        Z0     EMPILHA X            q0           [Z0 X]
2      a        X      rejeita simbolo      q_rejeita    [Z0 X]
Motivo: 'a' na coluna 2 esta fora do alfabeto
Resultado: REJEITADA
```

## Testes

| Cadeia | Resultado | Motivo |
| --- | --- | --- |
| `()` | aceita | abre e fecha |
| `(()())` | aceita | aninhamento e sequência |
| `( () )` | aceita | o léxico ignora espaços |
| `(` | rejeitada | sobrou X na pilha |
| `)` | rejeitada | fecha com a pilha em Z0 |
| `)(` | rejeitada | fecha antes de abrir |
| `(a)` | rejeitada | `a` não está no alfabeto |

O comando `build.bat` executa `tests/validas.txt` e `tests/invalidas.txt`.

## Por que a pilha é necessária

Um autômato finito só lembra o estado atual. A quantidade de `(` ainda sem par pode crescer o quanto a cadeia crescer, e um número fixo de estados não guarda essa contagem. Por isso a linguagem { `(`ⁿ `)`ⁿ | n ≥ 0 } não é regular.

O autômato com pilha guarda um X para cada `(` pendente e apaga esse X quando lê `)`. `(()())` só é aceita porque cada fechamento desfaz a abertura correspondente e, no fim, resta apenas Z0.
