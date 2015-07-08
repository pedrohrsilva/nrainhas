O Problema N-Rainhas consiste em colocar N rainhas num tabuleiro de xadrez NxN sem que nenhuma ataque a outra.

Existem diversas formas de resolver este problema, algumas ruins como por o método da Força Bruta que consiste em verificar todas as possibilidades. Considerando um tabuleiro 8x8, onde queremos colocar 8 rainhas, temos então (64!/55!) / 8! , que resulta numa bagatela de 247865260608 possibilidades.
Se fosse um tabuleiro 20x20, as possibilidades aumentam para (64!/43!)/ 8! = 5,208944175×10³¹.

Usando um pouco de lógica é possível reduzir estes números. Como nenhuma rainha fica na mesma linha/coluna que qualquer outra, pode-se reduzir o problema para a permutação de N posições. Tem-se que encontrar uma permutação da sequência de tamanho N, de 0...N, que satisfaça a condição de nenhum ataque.
Com isso a complexidade cai para N!, ou seja para um tabuleiro 8x8 temos 40320 possibilidades mas para um tabuleiro 20x20 ainda temos 2,432902008×10¹⁸ possibilidades.
