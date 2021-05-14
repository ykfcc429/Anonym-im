package hello.init;

import hello.util.BloomFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yankaifeng
 * 创建日期 2021/4/28
 * @since ZQ001
 */
@Component
@AllArgsConstructor
public class RandomNameInit implements ApplicationRunner {

    private final RedisTemplate<String,String> redisTemplate;

    private final BloomFilter bloomFilter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(redisTemplate.hasKey("adj") == null || !redisTemplate.hasKey("adj")) {
            String adjs = "温柔,内向,腼腆,害羞,率性,活泼,开朗,多情,热情,飘逸,可爱,慈祥,老实,暴躁,急躁,虚心,勤奋,热心,自信,任性,冲动,胆小,安静,憨厚,淡定,坚强,火爆,奔放,痴情,调皮,捣蛋,坏坏,安静,斯文,愉快," +
                    "兴奋,活泼,幸福,高兴,微笑,乐观,深情,快乐,激动,欢乐,欢快,痛苦,烦恼,紧张,忧郁,焦虑,苦闷,着急,难过,愤怒,失望,苦恼,悲伤,开心,不开心,无聊,孤独,空虚,寂寞,失恋,单身,发呆,发怒,失眠,睡不着,刚分手," +
                    "刚失恋,曾经爱过,曾深爱过,伤情,时尚,路过,飞翔,行走,奔跑,暴走,飞奔,销魂,火星上,星星上,月球上,奋斗,逆袭,拉风,咆哮,很拉风,要出家,想出家,呐喊,笑点低,没人理,会搭讪,爱搭讪,私奔,逃跑,越狱,打盹,喝醉," +
                    "微醺,求醉,买醉,耍酷,酷酷,灰常酷,很酷,非常酷,想发财,发财,犯傻,想旅行,旅行中,旅途中,想表白,不敢表白,从未表白,被表白,千年单身,一直单身,至今单身,还单身,道上混,玩手机,不要命,玩命,有爱心,热心肠," +
                    "会开车,闯红灯,唠叨,迷茫,彷徨,忐忑,茫然,失落,逃课,怕考试,想出国,考研,读研,没读研,爱逃课,挂过科,不爱学习,暗恋学妹,爱玩,贪玩,有腹肌,瘦瘦,小眼睛,眼睛小,鼻子大,大鼻子,眉毛粗,粗眉毛,帅气,帅呆," +
                    "好帅,近视,跑龙套,打酱油,八块腹肌,一身肌肉,满身肌肉,没有腹肌,爱听歌,爱跑步,爱看球,玩滑板,爱看书,爱热闹,爱吹牛,健身,爱健身,阳光,帅气,温暖,绅士,礼貌,宽容,大气,爱笑,温柔,不羁,追风,完美,耍酷," +
                    "魁梧,睿智,深沉,稳重,豪爽,低调,淡定,活泼,狂野,开朗,安静,高大,仗义,正直,博学,爽快,直爽,果断,豁达,沉着,儒雅,冷静,从容,谦逊,精明,干练,机灵,聪明,健壮,阳刚,慷慨,善良,坚强,乐观,心软,刚毅,俊逸," +
                    "俊秀,严肃,飘逸,成熟,沉稳,谦和,坚韧,憨厚,老实,含蓄,文雅,大方,强悍,强健,高大,深情,长情,踏实,痴情,体贴,细心,任性,独立,个性,另类,腹黑,腼腆,纯真,酷酷,怕老婆,冷冷,听话,乖乖,卖萌,逆袭,叛逆,鬼畜," +
                    "无邪,傻傻,逼格高,性感,坏坏,留胡子,小胡子,英俊,潇洒,风流,大力,爱运动,爱旅游,打篮球,踢足球,玩篮球,玩足球,霸气,豪气,威武,重情义,讲道义,重感情,爱喝酒,酒量大,酒量小,骑白马,风流倜傥,玉树临风," +
                    "神勇威武,文武双全,力能扛鼎,刀枪不入,侠义非凡,谦虚好学,聪明伶俐,慷慨大方,有情有义,有胆有识,谈吐大方,风度翩翩,气势凌人,气宇轩昂,英勇无比,千杯不醉,坐怀不乱,知识渊博,才高八斗,傲视众生,光明磊落," +
                    "文质彬彬,面冷心慈,豪情万千,温文尔雅,年轻有为,英姿勃勃,朝气蓬勃,不拘小节,胡子拉碴,阳光,帅气,温暖,绅士,礼貌,宽容,大气,爱笑,温柔,不羁,追风,完美,耍酷,魁梧,睿智,深沉,稳重,豪爽,低调,淡定,活泼," +
                    "狂野,开朗,安静,高大,仗义,正直,博学,爽快,直爽,果断,豁达,沉着,儒雅,冷静,从容,谦逊,精明,干练,机灵,聪明,健壮,阳刚,慷慨,善良,坚强,乐观,心软,刚毅,俊逸,俊秀,严肃,飘逸,成熟,沉稳,谦和,坚韧,憨厚," +
                    "老实,含蓄,文雅,强悍,闷骚,大方,强健,高大,深情,长情,踏实,痴情,体贴,细心,任性,独立,个性,另类,腹黑,腼腆,纯真,酷酷,怕老婆,冷冷,听话,乖乖,卖萌,逆袭,叛逆,鬼畜,无邪,傻傻,逼格高,性感,坏坏,留胡子," +
                    "小胡子,英俊,潇洒,风流,大力,爱运动,爱旅游,打篮球,踢足球,玩篮球,玩足球,霸气,豪气,威武,重情义,讲道义,重感情,爱喝酒,酒量大,酒量小,骑白马,风流倜傥,玉树临风,神勇威武,文武双全,力能扛鼎,刀枪不入," +
                    "侠义非凡,谦虚好学,聪明伶俐,慷慨大方,有情有义,有胆有识,谈吐大方,风度翩翩,气势凌人,气宇轩昂,英勇无比,千杯不醉,坐怀不乱,知识渊博,才高八斗,傲视众生,光明磊落,文质彬彬,面冷心慈,豪情万千,温文尔雅," +
                    "年轻有为,英姿勃勃,朝气蓬勃,不拘小节,胡子拉碴,有腹肌,瘦瘦,小眼睛,眼睛小,鼻子大,大鼻子,眉毛粗,粗眉毛,帅气,帅呆,好帅,近视,跑龙套,打酱油,八块腹肌,一身肌肉,满身肌肉,没有腹肌";
            for (String adj : adjs.split(","))
                redisTemplate.opsForSet().add("adj", adj);
        }
        String nouns = "西红柿番茄,菠萝,西瓜,香蕉,柚子,桔子,橙子,苹果,柠檬,梨子,椰子,葡萄,甘蔗,芒果,木瓜,柿子,石榴,槟榔,猕猴桃,蟠桃,山楂,香瓜,甜瓜,地瓜,李子,杨桃,枇杷,柑橘,荔枝,火龙果,南瓜,玉米,生菜,莴苣," +
                "大白菜,萝卜,胡萝卜,韭菜,木耳,豌豆,马铃薯,土豆,黄瓜,苦瓜,洋葱,芹菜,蘑菇,菠菜,莲藕,紫菜,茄子,香菜,青椒,四季豆,茴香,金针菇,扁豆,竹笋,绿豆,红豆,黄豆,毛豆,黄花菜,豆芽,丝瓜,大蒜,生姜,大葱,香菇," +
                "酱牛肉,酱肘子,小虾米,鸡蛋,鸭蛋,皮蛋,牛腩,罐头,豆腐,火腿肠,脆皮肠,小马驹,斑马,山羊,长颈鹿,大象,鸵鸟,骆驼,猴子,松鼠,蚂蚁,刺猬,企鹅,啄木鸟,小蝌蚪,青蛙,海龟,海豚,熊猫,大熊猫,小熊猫,野马,烈马," +
                "奔马,小狗,热带鱼,红金鱼,金鱼,仙人掌,仙人球,松树,柳树,圣诞树,筷子,碗,勺子,凳子,板凳,椅子,电脑桌,沙发,台灯,杯子,保温杯,茶壶,灯泡,日光灯,钱包,钥匙,蜡烛,手电筒,钥匙扣,热水瓶,开水瓶,水桶,水龙头," +
                "脸盆,镜子,火柴,打火机,抽屉,剪刀,枕头,毛巾,牙膏,电池,路灯,拖把,马克杯,砖头,鞭炮,硬币,水煮鱼,水煮肉,酸菜鱼,红烧肉,回锅肉,紫菜汤,米饭,稀饭,肉夹馍,灌汤包,小笼包,馒头,花卷,包子,油条,煎饼,煎饼果子," +
                "牛肉面,汉堡包,炒饭,炒粉,炒面,烤地瓜,红薯,烤红薯,泡面,鸡蛋面,乌冬面,牛肉面,饺子,凉面,春卷,羊肉串,汤圆,八宝粥,牛排,煎鸡蛋,卤蛋,盒饭,便当,花生,开心果,板栗,核桃,薯片,棒棒糖,吐司,烤土司,面包,烤面包," +
                "蛋挞,冰淇淋,冰棍,雪糕,饼干,麦片,爆米花,铅笔,钢笔,日记本,课本,橡皮擦,书包,饭卡,书签,电影票,草稿纸,作业本,草稿本,签字笔,啤酒,红酒,伏特加,烈酒,葡萄酒,香槟,汽水,豆浆,可乐,凉茶,白开水,乌龙茶,红茶," +
                "绿茶,咖啡,苦咖啡,茶叶,咖啡豆,卡布奇诺,足球,篮球,排球,羽毛球,乒乓球,显示器,键盘,数据线,充电器,移动电源,硬盘,鼠标,鼠标垫,投影仪,充值卡,火锅,麻辣香锅,铁板烧,葫芦,佛珠,手链,大脸猫,机器人,机器猫," +
                "上铺,创口贴,伤痕,伤疤,手术刀,饭盒,楼梯,楼房,电梯,口罩,灭火器,遥控器,闹钟,拐杖,感冒药,消炎药,山寨机,自行车,小摩托,单车,滑板,火车,警车,消防车,围巾,手套,帽子,风衣,沙滩裤,跑步鞋,人字拖,眼镜,墨镜," +
                "毛衣,针织衫,黑框眼镜,皮带,领带,西装,领结,冲锋衣,登山鞋,瀑布,树叶,松球,夕阳,太阳,大海,高山,荒野,双杠,单杠,哑铃,跑步机,打火机,香烟,匕首,小刀,弓箭,铁链,打火机,香烟,匕首,小刀,弓箭,铁链,围巾,手套," +
                "帽子,风衣,沙滩裤,跑步鞋,人字拖,眼镜,墨镜,毛衣,针织衫,黑框眼镜,皮带,领带,西装,领结,冲锋衣,登山鞋,小马驹,斑马,山羊,长颈鹿,大象,鸵鸟,骆驼,猴子,松鼠,蚂蚁,刺猬,企鹅,啄木鸟,小蝌蚪,青蛙,海龟,海豚," +
                "熊猫,大熊猫,小熊猫,野马,烈马,奔马";
        if(redisTemplate.hasKey("noun") == null || !redisTemplate.hasKey("noun")) {
            for (String noun : nouns.split(","))
                redisTemplate.opsForSet().add("noun", noun);
        }
        bloomFilter.create("name");
        for (String s : nouns.split(",")) {
            bloomFilter.put("name", s);
        }
    }
}
