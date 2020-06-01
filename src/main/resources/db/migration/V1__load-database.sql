CREATE TABLE if not exists player
(
    id        bigint       not null primary key auto_increment,
    nick_name varchar(100) not null,
    mana      int,
    life      int
);

CREATE TABLE if not exists bug
(
    id   bigint not null primary key auto_increment,
    mana int,
    life int
);

CREATE TABLE if not exists junior_card
(
    id          bigint       not null primary key auto_increment,
    title       varchar(100) not null,
    description varchar(255) not null,
    cost        int,
    life_damage int,
    passive     int,
    player_id   bigint,
    foreign key (player_id) references player (id)
);

CREATE TABLE if not exists bug_card
(
    id          bigint       not null primary key auto_increment,
    title       varchar(100) not null,
    description varchar(293) not null,
    cost        int,
    life_damage int,
    mana_damage int,
    bug_id      bigint,
    foreign key (bug_id) references bug (id)
);

INSERT INTO junior_card(title,
                        description,
                        cost,
                        life_damage,
                        passive)
VALUES ('Carta do Café', 'Use esta carta para aumentar em 4 pontos de mana.', null, null, 4),
       ('Change Experience Jr Power', 'Todos os Jrs se reuniram, cause 6 pontos de dano no BUG, perca 4 pontos de mana para utilizar este poder.', 4,
        6, null),
       ('Antivírus Ativo', 'Opa parece que algo vinha para afetar seu sistema, mas seu antivírus não deu mole, recupera 2 pontos de Mana e também' ||
                           ' golpeie o bug em 2 pontos.', null, 2, 2),
       ('Tech Lead Power', 'As coisas ficaram difíceis? Use o poder do TechLead para conseguir gerar 8 pontos de dano no BUG, porém isso irá lhe' ||
                           ' custar 6 pontos de mana.', 6, 8, null),
       ('Framework', 'Você mostrou que um framework bem utilizado pode realizar milagres em poupar linhas de código. Gaste 3 pontos de mana, e' ||
                     ' gere um dano de 3 pontos de vida no Bug.', 3, 3, null),
       ('Debug', 'Agora é hora de achar o bug que está escondido no código, vamos analisar linha a linha, mas vamos achar onde se esconde esse' ||
                 ' inseto maldito. Para isso gaste 6 pontos de mana, mas irá gerar um dano de 8 pontos no bug.', 6, 8, null),
       ('Stack Trace', 'Achamos uma stack trace, só pode estar por ai o bug que procuramos, para isso gaste 5 pontos de mana e gere um dano de 7' ||
                       ' pontos no seu bug.', 5, 7, null),
       ('Coffee Break ZUP',
        'Sim, está na hora de conversar com aquele seu amigo sobre o bug que está tentando resolver, essa carta irá te recuperar' ||
        ' 2 pontos de mana, e irá gerar 2 pontos de dano no bug.', null, 2, 2),
       ('Stack Overflow',
        'Se o Stack Overflow está conosco, quem estará contra? Você encontrou um post muito similar ao do Bug que estamos lutando,' ||
        ' agora é com você. Gaste 4 de mana para jogar essa carta e então gere um dano de 4 pontos de vida no bug.', 4, 4, null);

INSERT INTO bug_card(title,
                     description,
                     cost,
                     life_damage,
                     mana_damage)
VALUES ('Golpe do Não Consigo',
        'Sim, parece que o Bug está te sabotando, você acaba de perder 3 pontos de mana, e 2 pontos de energia, isso custou 4 pontos de mana para o' ||
        ' BUG', 4, 2, 3),
       ('Código Mal Escrito',
        'Variáveis mal nomeadas, código confuso, não consigo achar nada aqui dentro, o bug parece que vai se esconder melhor que nunca, isso vai te' ||
        ' custar muito esforço. O bug te tira 2 pontos de vida  e 2 pontos de mana. Isso custou 6 pontos de mana para o Bug', 6, 2, 2),
       ('Perdeu a Daily',
        'Sim, algo estranho aconteceu, só pode ser sobrenatural. Você perdeu a Daily, não sabe para onde seu time está levando o sistema, não' ||
        ' conseguiu aproveitar para mostrar alguns dos impedimentos. Perca 5 pontos de vida, isso irá custar para o bug 3 pontos de mana', 3, 5,
        null),
       ('EndPoint Batendo errado',
        'Puxa, que distração. Você está recebendo um erro 500, NullPointerException, tanto tempo perdido, e só agora entendeu que esse não era o' ||
        ' seu erro, era apenas o endpoint que estava batendo errado, perdeu tempo e tempo é vida. Perca 4 pontos de vida, e isso irá custar 4 pontos' ||
        ' de mana para o Bug', 4, 4, null),
       ('Perdendo Contato',
        'Você ficou dedicado algum tempo ao bug, não conversou com o time, não entrou no Change Experience Jr, e nem pediu ajuda para a equipe.' ||
        ' Acho que essa timidez irá lhe custar caro. Perca 4 pontos de vida e o bug irá perder 6 pontos de mana', 6, 4, null),
       ('Má administração do Tempo ',
        'Sim, parece que acumularam algumas atividades extras, você sabe que deveria ter feito, e agora as atividades que estavam previstas' ||
        ' certamente irão atrasar. Perca 3 pontos de vida para o bug, irá custar 1 ponto de mana.', 1, 3, null),
       ('Git',
        'Parece que você não deu Git Pull recentemente não é mesmo ? Vejo muito conflito entre os códigos da master e da sua Branch… isso vai te' ||
        ' custar algum tempo também, você perdeu 2 pontos de vida,  e isso custou ao bug apenas 1 ponto de mana.', 1, 2, null),
       ('Pandemia',
        'Você se arriscou, não respeitou a pandemia, tentou sair para trabalhar e adoeceu, virus e bug ? um belo estrago, você perdeu 6 pontos de' ||
        ' vida e isso custou 8 pontos de mana para o Bug.', 8, 6, null),
       ('Sem teste',
        'Hm…. achou que tinha me vencido e não me venceu não é mesmo ? Acho que alguem irá perder alguns belos pontos de vida 5 para ser mais exato' ||
        '. Para te pregar esta peça o Bug irá gastar 6 pontos de mana.', 6, 5, null);