# Banco de Dados 2

## Objetivo do Projeto 

Linguagem: Deve ser feito usando uma linguagem de apoio a escolha do grupo e SQL. O não cumprimento deste critério implica na nota do proieto zerada não importa a etapa
Implementação: Todos os requisitos abaixo DEVEM ser implementados com queres em SQL e
enviadas nara o banco de dados através da tuncão da biblioteca usada
Contexto: E um sistema de e-commerce de produtos diversos, o grupo tem liberdade criativa de
definir o contexto do e-commerce
O sistema deve ser criado de acordo com o modelo descrito abaixo:
• sistema conter a opção de criar e de destruir completamente o banco de dados;
O sistema deve possuir 20 produtos, 5 cargos e 100 clientes nativos;
O sistema deve possuir a opção de cadastrar apenas produtos e clientes;
Implemente 3 triggers:
Caso um vendedor venda mais de RS 1000.00 ele vai para a tabela de funcionário
especial e calcule o bônus em seu salário sendo 5% do valor vendido; Então crie um gatilho que emita uma mensagem informando auando de bônus salarial total será
necessario para custear
Caso um cliente combre mais de 500.00. adicione ele na tabela de clientes especiais
e adicione um valor de cash back de 2% do valor gasto: Então crie um gatilho que emita uma mensagem informando qual valor será necessário para lidar com todo
cash back.
Caso o valor do cash back do cliente especial for zerado remova ele da tabela de
clientes especiais,
molemente з usuarios:
Administrador - Com todas as permissões possíveis;
Gerente - com permissoes de busca, de apagar e de edição dos registros feitos;
Funcionário - Com permissão de adição de novos registros feitos e consultar os registros de venda.
Implemente 3 views diferentes usando JOINs e GROUP BY.
implemente os procedimentos:
Reajuste - Receba um reajuste de salário no formato percentual e uma categoria, então atribua o aumento salarial a todos os funcionários daquela categoria.
Sorteio - Sorteie aleatoriamente um cliente para que este cliente receba uma
premiacao. um voucher de 100 reais. caso este cliente esteja na tabela de clientes
especiais ele recebera um voucher de zoo reais ao inves de I00
Venda - Sempre que uma venda for feita reduza em 1 a quantidade do produto na
base de dados:
Estatísticas - Vá na tabela de vendas e exiba as seguintes estatísticas:
Produto mais vendido
Vendedor associado ao produto mais vendido
Produto menos vendido
valor ganho com o produto mais vendido
Mês de maior vendas e mês de menor vendas do produto mais vendido

• Valor ganho com o produto menos vendido
• Mês de maior vendas e mês de menor vendas do produto menos vendido
BANCO E DADOS empresa
cliente (id, nome, sexo, idade, nascimento)
clienteespecial(id, nome, sexo, idade, id_cliente(fk), cashback)
funcionario(id, nome, idade, sexo, cargo, salario, nascimento)
produto(id, nome, quantidade, descricao, valor)
venda(id, id_vendedor(fk), id_cliente(fk), data)
Constraints
Sexo deve ser 'm' ou '† ou 'o'
Cargo deve ser 'vendedor', 'gerente' ou 'CEO'
Na apresentação irei pedir para o grupo fazer duas alteração na constrait à minha escolha.
