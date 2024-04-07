# Bingo multicast

Aplicación colaborativa multicast que sirve para jugar al bingo, disponiendo de múltiples clientes y un servidor. Es desarrollada para la asignatura de Computación Distribuida del Grado de Ingeniería Informática de la Universidad de Santiago de Compostela.

Los clientes se ejecutan primero y cada uno elige, de forma aleatoria, 15 números (1-90) no repetidos para generar su cartón.

El servidor extraerá una de las 90 bolas mostrándolas por pantalla y enviando dicha información a todos los clientes.

Cuando un cliente recibe por parte del servidor todos los números correspondientes a su cartón envía un mensaje "Bingo" a todos los clientes y al propio servidor, así todos sabrán que el juego ha terminado.
