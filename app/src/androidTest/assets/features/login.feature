
Feature: Login
Perform login on email and password are inputted

  Scenario Outline: Input email and password in correct format
    Given Estou em Meus Anuncios
    When Seleciono o primeiro item
    And Aperto o botão Excluir
    And Aperto o botão Sim
    Then Verifico se Produto foi Deletado

  Examples:
    | email              | password   | see   |
    | espresso@spoon.com | bananacake | true  |
    | espresso@spoon.com | lemoncake  | false |
    | latte@spoon.com    | lemoncake  | true  |

