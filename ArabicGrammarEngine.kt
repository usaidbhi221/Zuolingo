package com.example.data.engine

import com.example.data.model.ArabicAnalysis
import com.example.data.model.PartOfSpeech

object ArabicGrammarEngine {

    // Pre-curated comprehensive Arabic Nahw & Sarf dictionary for instant offline responses
    private val offlineKnowledge: Map<String, ArabicAnalysis> = mapOf(
        // --- 1. HAROOF (حروف) ---
        "فِي" to ArabicAnalysis(
            word = "فِي",
            transliteration = "Fī",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "میں، اندر (ظرفیت کے لیے)",
            englishMeaning = "In, inside, within",
            nahw = "حَرْفُ جَرٍّ مَبْنِيٌّ عَلَى السُّكُونِ لَا مَحَلَّ لَهُ مِنَ الْإِعْرَابِ. يَجُرُّ الِاسْمَ الَّذِي بَعْدَهُ.",
            sarf = "الحروف جامدة لا يدخلها علم الصرف (حروف میں تصریف یا اشتقاق نہیں ہوتا)۔",
            qaida = "قاعدہ: حرفِ جر جب کسی اسم کے شروع میں آتا ہے تو اس اسم کو مجرور (زیر/کسرہ) کر دیتا ہے اور وہ جار و مجرور مل کر شبہ جملہ بنتے ہیں۔",
            examples = listOf(
                "الْقَلَمُ فِي الْحَقِيبَةِ" to "قلم بیگ میں ہے (The pen is in the bag)",
                "فِي الْمَسْجِدِ رِجَالٌ" to "مسجد میں لوگ ہیں (In the mosque there are men)"
            )
        ),
        "مِنْ" to ArabicAnalysis(
            word = "مِنْ",
            transliteration = "Min",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "سے، ابتدا، جزئیت",
            englishMeaning = "From, of",
            nahw = "حَرْفُ جَرٍّ مَبْنِيٌّ عَلَى السُّكُونِ (يحرك بالفتح لالتقاء الساكنين). يَجُرُّ الِاسْمَ بَعْدَهُ.",
            sarf = "حرف مبنی ہے، اس کا مادہ یا باب نہیں ہوتا۔",
            qaida = "قاعدہ: 'مِنْ' حرفِ جر ہے جو مکان، زمان یا بعض چیزوں کی ابتدا کو ظاہر کرتا ہے اور بعد والے اسم کو حالتِ جر (کسرہ) میں لے جاتا ہے۔",
            examples = listOf(
                "خَرَجَ مِنَ الْبَيْتِ" to "وہ گھر سے نکلا (He exited from the house)",
                "مِنَ الْمَسْجِدِ إِلَى السُّوقِ" to "مسجد سے بازار تک (From mosque to market)"
            )
        ),
        "إِلَى" to ArabicAnalysis(
            word = "إِلَى",
            transliteration = "Ilā",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "کی طرف، تک (انتہائے غایت)",
            englishMeaning = "To, towards, until",
            nahw = "حَرْفُ جَرٍّ مَبْنِيٌّ عَلَى السُّكُونِ الْمُقَدَّرِ عَلَى الْأَلِفِ. يَجُرُّ الاسْمَ بَعْدَهُ.",
            sarf = "حرف مبنی ہے، صرف کے اوزان سے خارج ہے۔",
            qaida = "قاعدہ: 'إِلَى' غایت اور منزلِ مقصود کو بیان کرنے والا حرفِ جر ہے، اس کے بعد آنے والا اسم ہمیشہ مجرور ہوتا ہے۔",
            examples = listOf(
                "ذَهَبَ الطَّالِبُ إِلَى الْمَدْرَسَةِ" to "طالب علم اسکول گیا (The student went to school)"
            )
        ),
        "عَلَى" to ArabicAnalysis(
            word = "عَلَى",
            transliteration = "'Alā",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "پر، اوپر (استعلاء کے لیے)",
            englishMeaning = "On, upon, above",
            nahw = "حَرْفُ جَرٍّ لِلِاسْتِعْلَاءِ مَبْنِيٌّ عَلَى السُّكُونِ الْمُقَدَّرِ. يَجُرُّ مَا بَعْدَهُ.",
            sarf = "حرف معانی، غیر مشتق۔",
            qaida = "قاعدہ: 'عَلَى' اوپر ہونے کی دلالت کرتا ہے اور حروفِ جارہ میں سے ہے جو بعد والے اسم کو کسرہ دیتا ہے۔",
            examples = listOf(
                "الْكِتَابُ عَلَى الْمَكْتَبِ" to "کتاب میز پر ہے (The book is on the desk)"
            )
        ),
        "إِنَّ" to ArabicAnalysis(
            word = "إِنَّ",
            transliteration = "Inna",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "بے شک، یقیناً (تاکید کے لیے)",
            englishMeaning = "Indeed, truly, verily",
            nahw = "حَرْفُ تَوْكِيدٍ وَنَصْبٍ (مِنَ الْأَحْرُفِ الْمُشَبَّهَةِ بِالْفِعْلِ)، يَنْصِبُ الِاسْمَ وَيَرْفَعُ الْخَبَرَ.",
            sarf = "حرف مبنی بر فتح، صیغوں سے پاک۔",
            qaida = "قاعدہ: 'إِنَّ' جملہ اسمیہ پر داخل ہوتا ہے، مبتدأ کو اپنا 'اسم' بنا کر منصوب (زبر) کرتا ہے اور خبر کو مرفوع (پیش) رکھتا ہے۔",
            examples = listOf(
                "إِنَّ اللَّهَ غَفُورٌ رَحِيمٌ" to "بے شک اللہ بخشنے والا مہربان ہے (Indeed Allah is Forgiving, Merciful)"
            )
        ),
        "بِ" to ArabicAnalysis(
            word = "بِـ",
            transliteration = "Bi",
            partOfSpeech = PartOfSpeech.HARF,
            urduMeaning = "ساتھ، ذریعے سے، قسم",
            englishMeaning = "With, by, in",
            nahw = "حَرْفُ جَرٍّ لِلْإِلْصَاقِ وَالِاسْتِعَانَةِ، مَبْنِيٌّ عَلَى الْكَسْرِ.",
            sarf = "حرف مفرد۔",
            qaida = "قاعدہ: باء کا حرف جر کسی کے ذریعے مدد حاصل کرنے یا ملاپ کے لیے آتا ہے، جیسے 'بِسْمِ اللَّهِ' میں اسم مجرور ہے۔",
            examples = listOf(
                "بِسْمِ اللَّهِ" to "اللہ کے نام سے (In the name of Allah)",
                "كَتَبْتُ بِالْقَلَمِ" to "میں نے قلم سے لکھا (I wrote with the pen)"
            )
        ),

        // --- 2. ASMAA (اسماء / Nouns) ---
        "الْحَمْدُ" to ArabicAnalysis(
            word = "الْحَمْدُ",
            transliteration = "Al-Ḥamdu",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "تمام تعریفیں اور شکر",
            englishMeaning = "All praise, gratitude and thanks",
            nahw = "مُبْتَدَأٌ مَرْفُوعٌ وَعَلَامَةُ رَفْعِهِ الضَّمَّةُ الظَّاهِرَةُ عَلَى آخِرِهِ، وَهُوَ اسْمٌ مَعْرِفَةٌ بِـ (الْـ).",
            sarf = "مادہ: ح-م-د، مصدر ثُلاثي مُجَرَّد، علی وزن 'فَعْل'.",
            qaida = "قاعدہ: جب جملے کی ابتدا کسی معرفہ اسم سے ہو تو وہ عام طور پر مبتدأ ہوتا ہے، اور مبتدأ ہمیشہ حالتِ رفع (پیش/ضمہ) میں ہوتا ہے۔",
            examples = listOf(
                "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ" to "تمام تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے (All praise is due to Allah, Lord of the worlds)"
            )
        ),
        "كِتَابٌ" to ArabicAnalysis(
            word = "كِتَابٌ",
            transliteration = "Kitābun",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "ایک کتاب (نکرہ)",
            englishMeaning = "A book",
            nahw = "اِسْمٌ مُفْرَدٌ نَكِرَةٌ، مَرْفُوعٌ بِالضَّمَّةِ الظَّاهِرَةِ (تنوين الضم).",
            sarf = "مادہ: ك-ت-ب، اسم مشتق، علی وزن 'فِعَال'، مأخوذ من الكتابة.",
            qaida = "قاعدہ: تنوین (دو زبر، دو زیر، دو پیش) اسمِ نکرہ کی پہچان ہے، اور اس کا مطلب عموماً 'ایک' یا کوئی غیر معین چیز ہوتی ہے۔",
            examples = listOf(
                "هَذَا كِتَابٌ مُفِيدٌ" to "یہ ایک فائدہ مند کتاب ہے (This is a useful book)"
            )
        ),
        "قَلَمٌ" to ArabicAnalysis(
            word = "قَلَمٌ",
            transliteration = "Qalamun",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "قلم، پین",
            englishMeaning = "A pen, pencil",
            nahw = "اِسْمٌ جَامِدٌ نَكِرَةٌ، مَرْفُوعٌ بِالتَّنْوِينِ.",
            sarf = "مادہ: ق-ل-م، اسْمُ آلَةٍ أَوْ اسْمُ ذَاتٍ، علی وزن 'فَعَل'.",
            qaida = "قاعدہ: اسم ذات ہے، جب اس کے ساتھ 'الـ' لگے تو 'الْقَلَمُ' معین قلم بن جائے گا اور تنوین ختم ہو جائے گی (ال اور تنوین ایک ساتھ نہیں آتے)۔",
            examples = listOf(
                "الْقَلَمُ جَدِيدٌ" to "قلم نیا ہے (The pen is new)"
            )
        ),
        "اللَّهُ" to ArabicAnalysis(
            word = "اللَّهُ",
            transliteration = "Allāh",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "اللہ تعالی، معبود برحق",
            englishMeaning = "Allah, The One True God",
            nahw = "لَفْظُ الْجَلَالَةِ، عَلَمٌ عَلَى الذَّاتِ الْإِلَهِيَّةِ، مُعْرَبٌ يَكُونُ مَرْفُوعاً أَوْ مَنْصُوباً أَوْ مَجْرُوراً بِحَسَبِ مَوْقِعِهِ فِي الْجُمْلَةِ.",
            sarf = "أصله 'الإله'، حذفت الهمزة تخفيفاً وعوضت عنها أل التعريف وأدغمت اللامان.",
            qaida = "قاعدہ: ادب کے پیش نظر اعراب میں 'لفظ الجلالة' کہا جاتا ہے، جیسے 'قَالَ اللَّهُ' میں فاعل مرفوع ہے۔",
            examples = listOf(
                "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ" to "اللہ آسمانوں اور زمین کا نور ہے (Allah is the Light of the heavens and the earth)"
            )
        ),
        "طَالِبٌ" to ArabicAnalysis(
            word = "طَالِبٌ",
            transliteration = "Ṭālibun",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "طلب علم کرنے والا، شاگرد",
            englishMeaning = "A student, seeker of knowledge",
            nahw = "اِسْمُ فَاعِلٍ، مُفْرَدٌ مُذَكَّرٌ نَكِرَةٌ، مَرْفُوعٌ.",
            sarf = "مادہ: ط-ل-ب، اِسْمُ فَاعِلٍ من الفعل الثُّلاثي 'طَلَبَ' علی وزن 'فَاعِل'.",
            qaida = "قاعدہ: ثلاثی مجرد سے اسمِ فاعل ہمیشہ 'فَاعِل' کے وزن پر آتا ہے، جیسے طَلَبَ سے طَالِبٌ، كَتَبَ سے كَاتِبٌ۔",
            examples = listOf(
                "الطَّالِبُ يَجْتَهِدُ فِي دَرْسِهِ" to "طالب علم اپنے سبق میں محنت کرتا ہے (The student works hard in his lesson)"
            )
        ),
        "بَيْتٌ" to ArabicAnalysis(
            word = "بَيْتٌ",
            transliteration = "Baytun",
            partOfSpeech = PartOfSpeech.ISM,
            urduMeaning = "گھر، مکان",
            englishMeaning = "A house, home",
            nahw = "اِسْمٌ مُفْرَدٌ نَكِرَةٌ مَرْفُوعٌ، جَمْعُهُ 'بُيُوتٌ'.",
            sarf = "مادہ: ب-ي-ت، اسم ثلاثي مجرد علی وزن 'فَعْل'.",
            qaida = "قاعدہ: اسم مفرد کا اعراب عام حالت میں ضمہ (پیش)، نصب میں فتحہ (زبر) اور جر میں کسرہ (زیر) کے ساتھ ظاہر ہوتا ہے۔",
            examples = listOf(
                "هَذَا بَيْتٌ جَمِيلٌ" to "یہ خوبصورت گھر ہے (This is a beautiful house)"
            )
        ),

        // --- 3. AF'AAL (أفعال / Verbs) ---
        "كَتَبَ" to ArabicAnalysis(
            word = "كَتَبَ",
            transliteration = "Kataba",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "اس نے لکھا (ماضی، ایک مذکر)",
            englishMeaning = "He wrote (Past tense, 3rd person singular masculine)",
            nahw = "فِعْلٌ مَاضٍ مَبْنِيٌّ عَلَى الْفَتْحِ الظَّاهِرِ. وَالْفَاعِلُ ضَمِيرٌ مُسْتَتِرٌ تَقْدِيرُهُ (هُوَ).",
            sarf = "مادہ: ك-ت-ب، بَابُ نَصَرَ يَنْصُرُ (فَعَلَ يَفْعُلُ)، صِيغَةُ: وَاحِدٌ مُذَكَّرٌ غَائِبٌ.",
            qaida = "قاعدہ: فعلِ ماضی ہمیشہ مبنی ہوتا ہے اور جب تک کوئی ضمیر متصل نہ جڑے، یہ فتحہ (زبر) پر مبنی رہتا ہے۔ فاعل ظاہر نہ ہو تو ضمیر مستتر مانی جاتی ہے۔",
            examples = listOf(
                "كَتَبَ التِّلْمِيذُ رِسَالَةً" to "شاگرد نے خط لکھا (The pupil wrote a letter)",
                "كَتَبَ اللَّهُ لَكُمُ الْفَوْزَ" to "اللہ نے تمہارے لیے کامیابی لکھ دی (Allah has decreed success for you)"
            )
        ),
        "يَكْتُبُ" to ArabicAnalysis(
            word = "يَكْتُبُ",
            transliteration = "Yaktubu",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "وہ لکھتا ہے یا لکھے گا",
            englishMeaning = "He writes / is writing / will write (Present/Future tense)",
            nahw = "فِعْلٌ مُضَارِعٌ مَرْفُوعٌ لِتَجَرُّدِهِ عَنِ النَّاصِبِ وَالْجَازِمِ، وَعَلَامَةُ رَفْعِهِ الضَّمَّةُ الظَّاهِرَةُ.",
            sarf = "مادہ: ك-ت-ب، باب نصر ينصر، صیغہ: واحد مذکر غائب، حرفِ مضارع 'ياء' مفتوح اور عین کلمہ 'تاء' مضموم ہے۔",
            qaida = "قاعدہ: فعلِ مضارع جب ناصب (جیسے أَنْ، لَنْ) اور جازم (جیسے لَمْ، لَا الناهية) سے خالی ہو تو مرفوع (پیش کے ساتھ) ہوتا ہے۔",
            examples = listOf(
                "الْوَلَدُ يَكْتُبُ الْوَاجِبَ" to "لڑکا ہوم ورک لکھ رہا ہے (The boy is writing homework)"
            )
        ),
        "ذَهَبَ" to ArabicAnalysis(
            word = "ذَهَبَ",
            transliteration = "Dhahaba",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "وہ گیا (ماضی)",
            englishMeaning = "He went (Past tense)",
            nahw = "فِعْلٌ مَاضٍ لَازِمٌ مَبْنِيٌّ عَلَى الْفَتْحِ. فَاعِلُهُ ضَمِيرٌ مُسْتَتِرٌ جَوَازاً تَقْدِيرُهُ هُوَ.",
            sarf = "مادہ: ذ-ه-ب، بَابُ فَتَحَ يَفْتَحُ (فَعَلَ يَفْعَلُ)، صِيغَةُ: وَاحِدٌ مُذَكَّرٌ غَائِبٌ.",
            qaida = "قاعدہ: یہ فعلِ لازم ہے، یعنی مفعول بہِ کا محتاج نہیں ہوتا بلکہ فاعل پر ہی بات مکمل ہو جاتی ہے یا حرفِ جر کے ذریعے متعدی ہوتا ہے۔",
            examples = listOf(
                "ذَهَبَ الْمُسَافِرُ إِلَى مَكَّةَ" to "مسافر مکہ مکرمہ گیا (The traveler went to Makkah)"
            )
        ),
        "قَالَ" to ArabicAnalysis(
            word = "قَالَ",
            transliteration = "Qāla",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "اس نے کہا (ماضی)",
            englishMeaning = "He said",
            nahw = "فِعْلٌ مَاضٍ مَبْنِيٌّ عَلَى الْفَتْحِ الظَّاهِرِ، وَجُمْلَةُ الْقَوْلِ بَعْدَهُ فِي مَحَلِّ نَصْبٍ مَفْعُولٌ بِهِ (مَقُولُ الْقَوْلِ).",
            sarf = "مادہ: ق-و-ل (أجوف واوي، أصله 'قَوَلَ' تحركت الواو وانفتح ما قبلها فقلبت ألفاً)، باب نصر ينصر.",
            qaida = "قاعدہ: 'قال' کے بعد جو بات کہی جائے اسے نحوی زبان میں 'مقول القول' کہتے ہیں اور وہ مفعول بہ کے حکم میں ہوتی ہے۔",
            examples = listOf(
                "قَالَ رَسُولُ اللَّهِ ﷺ" to "رسول اللہ ﷺ نے فرمایا (The Messenger of Allah said)"
            )
        ),
        "ضَرَبَ" to ArabicAnalysis(
            word = "ضَرَبَ",
            transliteration = "Ḍaraba",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "اس نے مارا / اس نے مثال بیان کی",
            englishMeaning = "He struck / hit / set forth an example",
            nahw = "فِعْلٌ مَاضٍ مُتَعَدٍّ مَبْنِيٌّ عَلَى الْفَتْحِ، يَطْلُبُ فَاعِلاً وَمَفْعُولاً بِهِ.",
            sarf = "مادہ: ض-ر-ب، بَابُ ضَرَبَ يَضْرِبُ (فَعَلَ يَفْعِلُ)، صیغہ: واحد مذکر غائب.",
            qaida = "قاعدہ: فعلِ متعدی فاعل کے ساتھ مفعول بہ کو بھی چاہتا ہے اور مفعول بہ ہمیشہ حالتِ نصب (زبر) میں ہوتا ہے، جیسے: ضَرَبَ زَيْدٌ مَثَلاً۔",
            examples = listOf(
                "ضَرَبَ اللَّهُ مَثَلاً" to "اللہ نے مثال بیان فرمائی (Allah set forth a parable)"
            )
        ),
        "سَمِعَ" to ArabicAnalysis(
            word = "سَمِعَ",
            transliteration = "Sami'a",
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "اس نے سنا",
            englishMeaning = "He heard / listened",
            nahw = "فِعْلٌ مَاضٍ مَبْنِيٌّ عَلَى الْفَتْحِ.",
            sarf = "مادہ: س-م-ع، بَابُ سَمِعَ يَسْمَعُ (فَعِلَ يَفْعَلُ)، صیغہ: واحد مذکر غائب، عین کلمہ 'میم' مکسور ہے۔",
            qaida = "قاعدہ: باب سمع کا عین کلمہ ماضی میں مکسور (زیر) اور مضارع میں مفتوح (زبر) ہوتا ہے: سَمِعَ يَسْمَعُ۔",
            examples = listOf(
                "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ" to "اللہ نے اس کی سن لی جس نے اس کی تعریف کی (Allah hears whoever praises Him)"
            )
        )
    )

    fun analyzeWord(query: String): ArabicAnalysis {
        val clean = query.trim().replace(Regex("[؟!.,]"), "")

        // Exact match with diacritics
        offlineKnowledge[clean]?.let { return it }

        // Match without diacritics
        val stripped = stripTashkeel(clean)
        for ((key, value) in offlineKnowledge) {
            if (stripTashkeel(key) == stripped) {
                return value
            }
        }

        // Dynamic Heuristic Parser
        return dynamicParseArabic(clean, stripped)
    }

    private fun dynamicParseArabic(word: String, stripped: String): ArabicAnalysis {
        // Detect Harf
        val haroofJar = setOf("من", "الى", "إلى", "عن", "على", "في", "رب", "ب", "ك", "ل", "حتى")
        if (stripped in haroofJar) {
            return ArabicAnalysis(
                word = word,
                transliteration = stripped,
                partOfSpeech = PartOfSpeech.HARF,
                urduMeaning = "حرفِ جر ہے (متعلقاتِ فعل یا اسم کو جوڑنے کے لیے)",
                englishMeaning = "Preposition (Particle of Jar)",
                nahw = "حَرْفُ جَرٍّ مَبْنِيٌّ لَا مَحَلَّ لَهُ مِنَ الْإِعْرَابِ، يَعْمَلُ الْجَرَّ فِي الِاسْمِ الَّذِي يَلِيهِ.",
                sarf = "الحروف مبنية ولا يدخلها التصريف.",
                qaida = "قاعدہ: یہ حروفِ جارہ میں سے ہے، اس کے بعد آنے والا اسم ہمیشہ مجرور (زیر والا) ہوتا ہے۔",
                examples = listOf("$word الْمَسْجِدِ" to "اس لفظ کا استعمال اسم مجرور کے ساتھ")
            )
        }

        // Detect Ism (Noun) by 'الـ' prefix or Tanween or Taa Marbutah
        val isDefinite = stripped.startsWith("ال")
        val hasTanween = word.endsWith("ٌ") || word.endsWith("ً") || word.endsWith("ٍ")
        val hasTaaMarbuta = stripped.endsWith("ة") || stripped.endsWith("ه")

        if (isDefinite || hasTanween || hasTaaMarbuta) {
            val root = extractProbableRoot(stripped.removePrefix("ال"))
            return ArabicAnalysis(
                word = word,
                transliteration = stripped,
                partOfSpeech = PartOfSpeech.ISM,
                urduMeaning = "اسم ہے (کسی چیز، شخص، جگہ یا صفت کا نام)",
                englishMeaning = "Noun / Adjective",
                nahw = if (isDefinite) {
                    "اِسْمٌ مَعْرِفَةٌ مُعَرَّفٌ بِـ (أَلْ)، يَكُونُ مَرْفُوعاً أَوْ مَنْصُوباً أَوْ مَجْرُوراً بِحَسَبِ مَوْقِعِهِ فِي الْجُمْلَةِ."
                } else {
                    "اِسْمٌ نَكِرَةٌ مُنَوَّنٌ، يَقْبَلُ الْإِعْرَابَ الثُّلَاثِيَّ (الرَّفْعَ، النَّصْبَ، الْجَرَّ)."
                },
                sarf = "مادہ (حروف اصلیہ): $root، اسم مشتق یا جامد ہے۔",
                qaida = if (isDefinite) {
                    "قاعدہ: جب کسی اسم پر 'الـ' آتا ہے تو وہ معرفہ بن جاتا ہے اور اس سے تنوین ساقط ہو جاتی ہے۔"
                } else {
                    "قاعدہ: تنوین اسم کی خاص علامتوں میں سے ہے، یہ افعال یا حروف پر کبھی داخل نہیں ہوتی۔"
                },
                examples = listOf(
                    "هَذَا $word" to "یہ ($word) ہے",
                    "رَأَيْتُ $word" to "میں نے ($word) دیکھا"
                )
            )
        }

        // Detect Fi'l Mudhari' (Starts with Ya, Ta, A, Na)
        if ((stripped.startsWith("ي") || stripped.startsWith("ت") || stripped.startsWith("ن") || stripped.startsWith("ا")) && stripped.length >= 4) {
            val root = extractProbableRoot(stripped.drop(1))
            return ArabicAnalysis(
                word = word,
                transliteration = stripped,
                partOfSpeech = PartOfSpeech.FIL,
                urduMeaning = "فعل مضارع ہے (حال یا مستقبل کا کام)",
                englishMeaning = "Imperfect Verb (Present / Future tense)",
                nahw = "فِعْلٌ مُضَارِعٌ مَرْفُوعٌ بِالضَّمَّةِ (إِذَا لَمْ يَسْبِقْهُ نَاصِبٌ وَلَا جَازِمٌ).",
                sarf = "مادہ: $root، حرفِ مضارع کے ساتھ صیغہ مضارع۔",
                qaida = "قاعدہ: فعلِ مضارع کے شروع میں حروفِ أَتَيْنَ میں سے کوئی حرف ہوتا ہے اور عام حالت میں یہ مرفوع ہوتا ہے۔",
                examples = listOf(
                    "$word الطَّالِبُ الدَّرْسَ" to "طالب علم سبق پڑھتا/لکھتا ہے"
                )
            )
        }

        // Default as Fi'l Maadhi or General Arabic word
        val root = extractProbableRoot(stripped)
        return ArabicAnalysis(
            word = word,
            transliteration = stripped,
            partOfSpeech = PartOfSpeech.FIL,
            urduMeaning = "فعل ماضی یا کلمہ عربیہ",
            englishMeaning = "Arabic Verb / Word",
            nahw = "كَلِمَةٌ عَرَبِيَّةٌ مُعْرَبَةٌ أَوْ مَبْنِيَّةٌ بِحَسَبِ السِّيَاقِ فِي الْجُمْلَةِ.",
            sarf = "مادہ (حروفِ اصلیہ): $root، اوزانِ عربیہ کے مطابق صیغہ۔",
            qaida = "قاعدہ: علمِ نحو کلمات کے آخری اعراب سے بحث کرتا ہے جبکہ علمِ صرف کلمات کی اندرونی بناوٹ اور صیغوں کی تبدیلی سکھاتا ہے۔",
            examples = listOf(
                "$word زَيْدٌ فِي الْأَمْرِ" to "مثال میں اس کلمے کا استعمال"
            )
        )
    }

    private fun stripTashkeel(text: String): String {
        return text.replace(Regex("[\u064B-\u065F\u0670]"), "")
    }

    private fun extractProbableRoot(text: String): String {
        val clean = text.replace(Regex("[أإآ]"), "ا")
        if (clean.length == 3) {
            return "${clean[0]}-${clean[1]}-${clean[2]}"
        }
        if (clean.length > 3) {
            val letters = clean.filter { it in "ابتثجحخدذرزسشصضطظعغفقكلمنهوي" }
            if (letters.length >= 3) {
                return "${letters[0]}-${letters[1]}-${letters[2]}"
            }
        }
        return "ف-ع-ل"
    }
}
