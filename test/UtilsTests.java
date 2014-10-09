import org.junit.Assert;
import org.junit.Test;
import ru.itbrains.gate.morph.MorphInfo;
import ru.itbrains.gate.morph.MorphParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UtilsTests {
  @Test
  public void parseFile() throws Exception {
    File file = new File("files/words.txt");
    for (String str : Utils.parseFile(file)) {
      System.out.println(str);
    }
  }

  @Test
  public void tokenize() {
    String[] strings = {".человек", "!собаке", "друг?", "-это", "не...", " знают??", "!fef "};
    String[] res = {"человек", "собаке", "друг", "это", "не", "знают", "fef"};
    for (int i = 0; i < strings.length; i++) {
      String str = Utils.tokenize(strings[i]);
      Assert.assertTrue(String.format("<%s> != <%s>", str , res[i]), str.equals(res[i]));
    }
  }

  @Test
  public void checkSpeed() throws IOException {
    String text = ", – корчась от страху, ответила дама.\n" +
        "– Снимайте штаны, сударыня, – облегченно молвил Филипп Филиппович и указал на высокий белый эшафот в углу.\n" +
        "– Клянусь, профессор, – бормотала дама, дрожащими пальцами расстегивая какие-то кнопки на поясе, – этот Мориц... я вам признаюсь, как на духу...\n" +
        "– «От Севильи до Гренады...» – рассеянно запел Филипп Филиппович и нажал педаль в мраморном умывальнике. Зашумела вода.\n" +
        "– Богом клянусь! – говорила дама, и живые пятна сквозь искусственные продирались на ее щеках. – Я знаю, что это моя последняя страсть... Ведь это такой негодяй! О, профессор! Он карточный шулер, это знает вся Москва. Он не может пропустить ни одной гнусной модистки. Ведь он так дьявольски молод! – Дама бормотала и выбрасывала из-под шумящих юбок скомканный кружевной клок.\n";

    String text2 = ", – корчась от страху, ответила дама.\n" +
        "– Снимайте штаны, сударыня, – облегченно молвил Филипп Филиппович и указал на высокий белый эшафот в углу.\n" +
        "– Клянусь, профессор, – бормотала дама, дрожащими пальцами расстегивая какие-то кнопки на поясе, – этот Мориц... я вам признаюсь, как на духу...\n" +
        "– «От Севильи до Гренады...» – рассеянно запел Филипп Филиппович и нажал педаль в мраморном умывальнике. Зашумела вода.\n" +
        "– Богом клянусь! – говорила дама, и живые пятна сквозь искусственные продирались на ее щеках. – Я знаю, что это моя последняя страсть... Ведь это такой негодяй! О, профессор! Он карточный шулер, это знает вся Москва. Он не может пропустить ни одной гнусной модистки. Ведь он так дьявольски молод! – Дама бормотала и выбрасывала из-под шумящих юбок скомканный кружевной клок.\n" +
        "Пес совершенно затуманился, и все в голове пошло у него кверху ногами.\n" +
        "«Ну вас к черту, – мутно подумал он, положил голову на лапы и задремал от стыда, – и стараться не буду понять, что это за штука, все равно не пойму».\n" +
        "Очнулся он от звона и увидел, что Филипп Филиппович швырнул в таз какие-то сияющие трубки.\n" +
        "Пятнистая дама, прижимая руки к груди, с надеждой глядела на Филиппа Филипповича. Тот важно нахмурился и, сев за стол, что-то записал.\n" +
        "– Я вам, сударыня, вставлю яичники обезьяны, – объявил он и посмотрел строго.\n" +
        "– Ах, профессор, неужели обезьяны?\n" +
        "– Да, – непреклонно ответил Филипп Филиппович.\n" +
        "– Когда же операция? – бледнея, слабым голосом спрашивала дама.\n" +
        "– «От Севильи до Гренады...» угум... В понедельник. Ляжете в клинику с утра, мой ассистент приготовит вас.\n" +
        "– Ах, я не хочу в клинику. Нельзя ли у вас, профессор?\n" +
        "– Видите ли, у себя я делаю операции лишь в крайних случаях. Это будет стоить очень дорого – пятьдесят червонцев.\n" +
        "– Я согласна, профессор!\n" +
        "Опять загремела вода, колыхнулась шляпа с перьями, потом появилась какая-то лысая, как тарелка, голова и обняла Филиппа Филипповича. Пес дремал, тошнота прошла, пес наслаждался утихшим боком и теплом, даже всхрапнул и успел увидать кусочек приятного сна: будто бы он вырвал у совы целый пук перьев из хвоста... Потом взволнованный голос тявкнул над головой:\n" +
        "– Я – известный общественный деятель, профессор! Что же теперь делать?\n" +
        "– Господа! – возмущенно кричал Филипп Филиппович. – Нельзя же так! Нужно сдерживать себя! Сколько ей лет?\n" +
        "– Четырнадцать, профессор... Вы понимаете, огласка погубит меня. На днях я должен получить командировку в Лондон...\n" +
        "– Да ведь я же не юрист, голубчик... Ну, подождите два года и женитесь на ней.\n" +
        "– Женат я, профессор!\n" +
        "– Ах, господа, господа!..\n" +
        "Двери открывались, сменялись лица, гремели инструменты в шкафу, и Филипп Филиппович работал не покладая рук.\n" +
        "«Похабная квартирка, – думал пес, – но до чего хорошо! А на какого черта я ему понадобился? Неужли же жить оставит? Вот чудак! Да ведь ему только глазом мигнуть, он таким бы псом обзавелся, что ахнуть! А может, я и красивый. Видно, мое счастье! А сова эта дрянь. Наглая».\n" +
        "Окончательно пес очнулся глубоким вечером, когда звоночки прекратились, и как раз в то мгновенье, когда дверь впустила особенных посетителей. Их было сразу четверо. Все молодые люди, и все одеты очень скромно.\n" +
        "«Этим что нужно?» – неприязненно и удивленно подумал пес. Гораздо более неприязненно встретил гостей Филипп Филиппович. Он стоял у письменного стола и смотрел на них, как полководец на врагов. Ноздри его ястребиного носа раздувались. Вошедшие потоптались на ковре.\n" +
        "– Мы к вам, профессор, – заговорил тот из них, у кого на голове возвышалась на четверть аршина копна густейших вьющихся черных волос, – вот по какому делу...\n" +
        "– Вы, господа, напрасно ходите без калош в такую погоду, – перебил его наставительно Филипп Филиппович, – во-первых, вы простудитесь, а во-вторых, вы наследили мне на коврах, а все ковры у меня персидские.\n" +
        "Тот, с копной, умолк, и все четверо в изумлении уставились на Филиппа Филипповича. Молчание продолжалось несколько секунд, и прерывал его лишь стук пальцев Филиппа Филипповича по расписному деревянному блюду на столе.\n" +
        "– Во-первых, мы не господа, – молвил наконец самый юный из четверых – персикового вида.\n" +
        "– Во-первых, – перебил и его Филипп Филиппович, – вы мужчина или женщина?\n" +
        "Четверо вновь смолкли и открыли рты. На этот раз опомнился первым тот, с копной.\n" +
        "– Какая разница, товарищ? – спросил он горделиво.\n" +
        "– Я – женщина, – признался персиковый юноша в кожаной куртке и сильно покраснел. Вслед за ним покраснел почему-то густейшим образом один из вошедших – блондин в папахе.\n" +
        "– В таком случае вы можете оставаться в кепке, а вас, милостивый государь, попрошу снять ваш головной убор, – внушительно сказал Филипп Филиппович.\n" +
        "– Я не «милостивый государь», – возмущенно пробормотал блондин, снимая папаху.\n" +
        "– Мы пришли к вам, – вновь начал черный с копной.\n" +
        "– Прежде всего, кто это «мы»?\n" +
        "– Мы – новое домоуправление нашего дома, – в сдержанной ярости заговорил черный. – Я – Швондер, она – Вяземская, он – товарищ Пеструхин и Жаровкин. И вот мы...\n" +
        "– Это вас вселили в квартиру Федора Павловича Саблина?\n" +
        "– Нас, – ответил Швондер.\n" +
        "– Боже! Пропал калабуховский дом! – в отчаянии воскликнул Филипп Филиппович и всплеснул руками.\n" +
        "– Что вы, профессор, смеетесь? – возмутился Швондер.\n" +
        "– Какое там смеюсь! Я в полном отчаянии! – крикнул Филипп Филиппович. – Что ж теперь будет с паровым отоплением!\n" +
        "– Вы издеваетесь, профессор Преображенский!\n" +
        "– По какому делу вы пришли ко мне, говорите как можно скорее, я сейчас иду обедать.\n" +
        "– Мы – управление дома, – с ненавистью заговорил Швондер, – пришли к вам после общего собрания жильцов нашего дома, на котором стоял вопрос об уплотнении квартир дома.\n" +
        "– Кто на ком стоял? – крикнул Филипп Филиппович. – Потрудитесь излагать ваши мысли яснее";
    MorphParser parser = new MorphParser("./");

    long start, total;

    start = System.currentTimeMillis();
    Iterable<String> strings = Utils.normalizedWords(parser, text);
    total = System.currentTimeMillis() - start;
    System.out.println(1.0 * total / 1000);

//    String text2; //= text + text + text + text + text;
    start = System.currentTimeMillis();
    strings = Utils.normalizedWords(parser, text);
    total = System.currentTimeMillis() - start;
    System.out.println(1.0 * total / 1000);
//    for (String string : strings) {
//      System.out.print(string + ", ");
//    }
  }
}