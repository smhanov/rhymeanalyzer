package backend;


public class Ruleset {
	
	public static final String Anything = "";
	public static final String Nothing = " ";
	public static final String Pause = " ";
	public static final String Silent = "";
	
	public static final String[] Cardinals = {"Z IH1 R OW0", "W AH1 N", "T UW1", "TH R IY1", "F OW1 R",	"F AY1 V", "S IH1 K S", 
		"S EH1 V AH0 N", "EY1 T", "N AY1 N", "T EH1 N", "IY0 L EH1 V AH0 N", "T W EH1 L V", "TH ER1 T IY2 N", "F OW1 R T IY2 N", "F IH1 F T IY2 N",
		"S IH1 K S T IY2 N", "S EH1 V AH0 N T IY2 N", "EY2 T IY1 N", "N AY2 N T IY1 N"};
		
	public static final String[] Twenties  = {"T W EH1 N T IY0", "TH ER1 T IY0", "F AO1 R T IY", "F IH1 F T IY0", "S IH1 K S T IY0", 
		"S EH1 V AH0 N T IY0", "EY1 T IY0", "N AY1 N T IY2"};
	
	private RuleHolder[] holders;
	
	public Ruleset() {
		
		holders = new RuleHolder[27];
		for (char c='A'; c<='Z'; c++) {
			holders[c-'A'] = new RuleHolder(String.valueOf(c));
		}
		holders[26] = new RuleHolder("punct");
		
		holders['A'-'A'].addRule(Anything,	"A",		Nothing,	"AX"	);
		holders['A'-'A'].addRule(Nothing,	"ARE",		Nothing,	"AAr"	);
		holders['A'-'A'].addRule(Nothing,	"AR",		"O",		"AXr"	);
		holders['A'-'A'].addRule(Anything,	"AR",		"#",		"EHr"	);
		holders['A'-'A'].addRule("^",		"AS",		"#",		"EYs"	);
		holders['A'-'A'].addRule(Anything,	"A",		"WA",		"AX"	);
		holders['A'-'A'].addRule(Anything,	"AW",		Anything,	"AO"	);
		holders['A'-'A'].addRule(" :",		"ANY",		Anything,	"EHnIY"	);
		holders['A'-'A'].addRule(Anything,	"A",		"^+#",		"EY"	);
		holders['A'-'A'].addRule("#:",		"ALLY",		Anything,	"AXlIY"	);
		holders['A'-'A'].addRule(Nothing,	"AL",		"#",		"AXl"	);
		holders['A'-'A'].addRule(Anything,	"AGAIN",	Anything,	"AXgEHn");
		holders['A'-'A'].addRule("#:",		"AG",		"E",		"IHj"	);
		holders['A'-'A'].addRule(Anything,	"A",		"^+:#",		"AE"	);
		holders['A'-'A'].addRule(" :",		"A",		"^+ ",		"EY"	);
		holders['A'-'A'].addRule(Anything,	"A",		"^%",		"EY"	);
		holders['A'-'A'].addRule(Nothing,	"ARR",		Anything,	"AXr"	);
		holders['A'-'A'].addRule(Anything,	"ARR",		Anything,	"AEr"	);
		holders['A'-'A'].addRule(" :",		"AR",		Nothing,	"AAr"	);
		holders['A'-'A'].addRule(Anything,	"AR",		Nothing,	"ER"	);
		holders['A'-'A'].addRule(Anything,	"AR",		Anything,	"AAr"	);
		holders['A'-'A'].addRule(Anything,	"AIR",		Anything,	"EHr"	);
		holders['A'-'A'].addRule(Anything,	"AI",		Anything,	"EY"	);
		holders['A'-'A'].addRule(Anything,	"AY",		Anything,	"EY"	);
		holders['A'-'A'].addRule(Anything,	"AU",		Anything,	"AO"	);
		holders['A'-'A'].addRule("#:",		"AL",		Nothing,	"AXl"	);
		holders['A'-'A'].addRule("#:",		"ALS",		Nothing,	"AXlz"	);
		holders['A'-'A'].addRule(Anything,	"ALK",		Anything,	"AOk"	);
		holders['A'-'A'].addRule(Anything,	"AL",		"^",		"AOl"	);
		holders['A'-'A'].addRule(" :",		"ABLE",		Anything,	"EYbAXl");
		holders['A'-'A'].addRule(Anything,	"ABLE",		Anything,	"AXbAXl");
		holders['A'-'A'].addRule(Anything,	"ANG",		"+",		"EYnj"	);
		holders['A'-'A'].addRule(Anything,	"A",		Anything,	"AE"	);
		
		holders['B'-'A'].addRule(Nothing,	"BE",		"^#",		"bIH"	);
		holders['B'-'A'].addRule(Anything,	"BEING",	Anything,	"bIYIHNG");
		holders['B'-'A'].addRule(Nothing,	"BOTH",		Nothing,	"bOWTH"	);
		holders['B'-'A'].addRule(Nothing,	"BUS",		"#",		"bIHz"	);
		holders['B'-'A'].addRule(Anything,	"BUIL",		Anything,	"bIHl"	);
		holders['B'-'A'].addRule(Anything,	"B",		Anything,	"b"	);
		
		holders['C'-'A'].addRule(Nothing,	"CH",		"^",		"k"	);
		holders['C'-'A'].addRule("^E",		"CH",		Anything,	"k"	);
		holders['C'-'A'].addRule(Anything,	"CH",		Anything,	"CH"	);
		holders['C'-'A'].addRule(" S",		"CI",		"#",		"sAY"	);
		holders['C'-'A'].addRule(Anything,	"CI",		"A",		"SH"	);
		holders['C'-'A'].addRule(Anything,	"CI",		"O",		"SH"	);
		holders['C'-'A'].addRule(Anything,	"CI",		"EN",		"SH"	);
		holders['C'-'A'].addRule(Anything,	"C",		"+",		"s"	);
		holders['C'-'A'].addRule(Anything,	"CK",		Anything,	"k"	);
		holders['C'-'A'].addRule(Anything,	"COM",		"%",		"kAHm"	);
		holders['C'-'A'].addRule(Anything,	"C",		Anything,	"k"	);
		
		holders['D'-'A'].addRule("#:",		"DED",		Nothing,	"dIHd"	);
		holders['D'-'A'].addRule(".E",		"D",		Nothing,	"d"	);
		holders['D'-'A'].addRule("#:^E",	"D",		Nothing,	"t"	);
		holders['D'-'A'].addRule(Nothing,	"DE",		"^#",		"dIH"	);
		holders['D'-'A'].addRule(Nothing,	"DO",		Nothing,	"dUW"	);
		holders['D'-'A'].addRule(Nothing,	"DOES",		Anything,	"dAHz"	);
		holders['D'-'A'].addRule(Nothing,	"DOING",	Anything,	"dUWIHNG");
		holders['D'-'A'].addRule(Nothing,	"DOW",		Anything,	"dAW"	);
		holders['D'-'A'].addRule(Anything,	"DU",		"A",		"jUW"	);
		holders['D'-'A'].addRule(Anything,	"D",		Anything,	"d"	);
		
		holders['E'-'A'].addRule("#:",		"E",		Nothing,	Silent	);
		holders['E'-'A'].addRule("':^",		"E",		Nothing,	Silent	);
		holders['E'-'A'].addRule(" :",		"E",		Nothing,	"IY"	);
		holders['E'-'A'].addRule("#",		"ED",		Nothing,	"d"	);
		holders['E'-'A'].addRule("#:",		"E",		"D ",		Silent	);
		holders['E'-'A'].addRule(Anything,	"EV",		"ER",		"EHv"	);
		holders['E'-'A'].addRule(Anything,	"E",		"^%",		"IY"	);
		holders['E'-'A'].addRule(Anything,	"ERI",		"#",		"IYrIY"	);
		holders['E'-'A'].addRule(Anything,	"ERI",		Anything,	"EHrIH"	);
		holders['E'-'A'].addRule("#:",		"ER",		"#",		"ER"	);
		holders['E'-'A'].addRule(Anything,	"ER",		"#",		"EHr"	);
		holders['E'-'A'].addRule(Anything,	"ER",		Anything,	"ER"	);
		holders['E'-'A'].addRule(Nothing,	"EVEN",		Anything,	"IYvEHn");
		holders['E'-'A'].addRule("#:",		"E",		"W",		Silent	);
		holders['E'-'A'].addRule("T",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("S",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("R",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("D",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("L",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("Z",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("N",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("J",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("TH",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("CH",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule("SH",		"EW",		Anything,	"UW"	);
		holders['E'-'A'].addRule(Anything,	"EW",		Anything,	"yUW"	);
		holders['E'-'A'].addRule(Anything,	"E",		"O",		"IY"	);
		holders['E'-'A'].addRule("#:S",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:C",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:G",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:Z",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:X",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:J",		"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:CH",	"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:SH",	"ES",		Nothing,	"IHz"	);
		holders['E'-'A'].addRule("#:",		"E",		"S ",		Silent	);
		holders['E'-'A'].addRule("#:",		"ELY",		Nothing,	"lIY"	);
		holders['E'-'A'].addRule("#:",		"EMENT",	Anything,	"mEHnt"	);
		holders['E'-'A'].addRule(Anything,	"EFUL",		Anything,	"fUHl"	);
		holders['E'-'A'].addRule(Anything,	"EE",		Anything,	"IY"	);
		holders['E'-'A'].addRule(Anything,	"EARN",		Anything,	"ERn"	);
		holders['E'-'A'].addRule(Nothing,	"EAR",		"^",		"ER"	);
		holders['E'-'A'].addRule(Anything,	"EAD",		Anything,	"EHd"	);
		holders['E'-'A'].addRule("#:",		"EA",		Nothing,	"IYAX"	);
		holders['E'-'A'].addRule(Anything,	"EA",		"SU",		"EH"	);
		holders['E'-'A'].addRule(Anything,	"EA",		Anything,	"IY"	);
		holders['E'-'A'].addRule(Anything,	"EIGH",		Anything,	"EY"	);
		holders['E'-'A'].addRule(Anything,	"EI",		Anything,	"IY"	);
		holders['E'-'A'].addRule(Nothing,	"EYE",		Anything,	"AY"	);
		holders['E'-'A'].addRule(Anything,	"EY",		Anything,	"IY"	);
		holders['E'-'A'].addRule(Anything,	"EU",		Anything,	"yUW"	);
		holders['E'-'A'].addRule(Anything,	"E",		Anything,	"EH"	);
		
		holders['F'-'A'].addRule(Anything,	"FUL",		Anything,	"fUHl"	);
		holders['F'-'A'].addRule(Anything,	"F",		Anything,	"f"	);
		
		holders['G'-'A'].addRule(Anything,	"GIV",		Anything,	"gIHv"	);
		holders['G'-'A'].addRule(Nothing,	"G",		"I^",		"g"	);
		holders['G'-'A'].addRule(Anything,	"GE",		"T",		"gEH"	);
		holders['G'-'A'].addRule("SU",		"GGES",		Anything,	"gjEHs"	);
		holders['G'-'A'].addRule(Anything,	"GG",		Anything,	"g"	);
		holders['G'-'A'].addRule(" B#",		"G",		Anything,	"g"	);
		holders['G'-'A'].addRule(Anything,	"G",		"+",		"j"	);
		holders['G'-'A'].addRule(Anything,	"GREAT",	Anything,	"grEYt"	);
		holders['G'-'A'].addRule("#",		"GH",		Anything,	Silent	);
		holders['G'-'A'].addRule(Anything,	"G",		Anything,	"g"	);
		
		holders['H'-'A'].addRule(Nothing,	"HAV",		Anything,	"hAEv"	);
		holders['H'-'A'].addRule(Nothing,	"HERE",		Anything,	"hIYr"	);
		holders['H'-'A'].addRule(Nothing,	"HOUR",		Anything,	"AWER"	);
		holders['H'-'A'].addRule(Anything,	"HOW",		Anything,	"hAW"	);
		holders['H'-'A'].addRule(Anything,	"H",		"#",		"h"	);
		holders['H'-'A'].addRule(Anything,	"H",		Anything,	Silent	);
		
		holders['I'-'A'].addRule(Nothing,	"IN",		Anything,	"IHn"	);
		holders['I'-'A'].addRule(Nothing,	"I",		Nothing,	"AY"	);
		holders['I'-'A'].addRule(Anything,	"IN",		"D",		"AYn"	);
		holders['I'-'A'].addRule(Anything,	"IER",		Anything,	"IYER"	);
		holders['I'-'A'].addRule("#:R",		"IED",		Anything,	"IYd"	);
		holders['I'-'A'].addRule(Anything,	"IED",		Nothing,	"AYd"	);
		holders['I'-'A'].addRule(Anything,	"IEN",		Anything,	"IYEHn"	);
		holders['I'-'A'].addRule(Anything,	"IE",		"T",		"AYEH"	);
		holders['I'-'A'].addRule(" :",		"I",		"%",		"AY"	);
		holders['I'-'A'].addRule(Anything,	"I",		"%",		"IY"	);
		holders['I'-'A'].addRule(Anything,	"IE",		Anything,	"IY"	);
		holders['I'-'A'].addRule(Anything,	"I",		"^+:#",		"IH"	);
		holders['I'-'A'].addRule(Anything,	"IR",		"#",		"AYr"	);
		holders['I'-'A'].addRule(Anything,	"IZ",		"%",		"AYz"	);
		holders['I'-'A'].addRule(Anything,	"IS",		"%",		"AYz"	);
		holders['I'-'A'].addRule(Anything,	"I",		"D%",		"AY"	);
		holders['I'-'A'].addRule("+^",		"I",		"^+",		"IH"	);
		holders['I'-'A'].addRule(Anything,	"I",		"T%",		"AY"	);
		holders['I'-'A'].addRule("#:^",		"I",		"^+",		"IH"	);
		holders['I'-'A'].addRule(Anything,	"I",		"^+",		"AY"	);
		holders['I'-'A'].addRule(Anything,	"IR",		Anything,	"ER"	);
		holders['I'-'A'].addRule(Anything,	"IGH",		Anything,	"AY"	);
		holders['I'-'A'].addRule(Anything,	"ILD",		Anything,	"AYld"	);
		holders['I'-'A'].addRule(Anything,	"IGN",		Nothing,	"AYn"	);
		holders['I'-'A'].addRule(Anything,	"IGN",		"^",		"AYn"	);
		holders['I'-'A'].addRule(Anything,	"IGN",		"%",		"AYn"	);
		holders['I'-'A'].addRule(Anything,	"IQUE",		Anything,	"IYk"	);
		holders['I'-'A'].addRule(Anything,	"I",		Anything,	"IH"	);
		
		holders['J'-'A'].addRule(Anything,	"J",		Anything,	"j"	);
		
		holders['K'-'A'].addRule(Nothing,	"K",		"N",		Silent	);
		holders['K'-'A'].addRule(Anything,	"K",		Anything,	"k"	);
		
		holders['L'-'A'].addRule(Anything,	"LO",		"C#",		"lOW"	);
		holders['L'-'A'].addRule("L",		"L",		Anything,	Silent	);
		holders['L'-'A'].addRule("#:^",		"L",		"%",		"AXl"	);
		holders['L'-'A'].addRule(Anything,	"LEAD",		Anything,	"lIYd"	);
		holders['L'-'A'].addRule(Anything,	"L",		Anything,	"l"	);
		
		holders['M'-'A'].addRule(Anything,	"MOV",		Anything,	"mUWv"	);
		holders['M'-'A'].addRule(Anything,	"M",		Anything,	"m"	);
		
		holders['N'-'A'].addRule("E",		"NG",		"+",		"nj"	);
		holders['N'-'A'].addRule(Anything,	"NG",		"R",		"NGg"	);
		holders['N'-'A'].addRule(Anything,	"NG",		"#",		"NGg"	);
		holders['N'-'A'].addRule(Anything,	"NGL",		"%",		"NGgAXl");
		holders['N'-'A'].addRule(Anything,	"NG",		Anything,	"NG"	);
		holders['N'-'A'].addRule(Anything,	"NK",		Anything,	"NGk"	);
		holders['N'-'A'].addRule(Nothing,	"NOW",		Nothing,	"nAW"	);
		holders['N'-'A'].addRule(Anything,	"N",		Anything,	"n"	);
		
		holders['O'-'A'].addRule(Anything,	"OF",		Nothing,	"AXv"	);
		holders['O'-'A'].addRule(Anything,	"OROUGH",	Anything,	"EROW"	);
		holders['O'-'A'].addRule("#:",		"OR",		Nothing,	"ER"	);
		holders['O'-'A'].addRule("#:",		"ORS",		Nothing,	"ERz"	);
		holders['O'-'A'].addRule(Anything,	"OR",		Anything,	"AOr"	);
		holders['O'-'A'].addRule(Nothing,	"ONE",		Anything,	"wAHn"	);
		holders['O'-'A'].addRule(Anything,	"OW",		Anything,	"OW"	);
		holders['O'-'A'].addRule(Nothing,	"OVER",		Anything,	"OWvER"	);
		holders['O'-'A'].addRule(Anything,	"OV",		Anything,	"AHv"	);
		holders['O'-'A'].addRule(Anything,	"O",		"^%",		"OW"	);
		holders['O'-'A'].addRule(Anything,	"O",		"^EN",		"OW"	);
		holders['O'-'A'].addRule(Anything,	"O",		"^I#",		"OW"	);
		holders['O'-'A'].addRule(Anything,	"OL",		"D",		"OWl"	);
		holders['O'-'A'].addRule(Anything,	"OUGHT",	Anything,	"AOt"	);
		holders['O'-'A'].addRule(Anything,	"OUGH",		Anything,	"AHf"	);
		holders['O'-'A'].addRule(Nothing,	"OU",		Anything,	"AW"	);
		holders['O'-'A'].addRule("H",		"OU",		"S#",		"AW"	);
		holders['O'-'A'].addRule(Anything,	"OUS",		Anything,	"AXs"	);
		holders['O'-'A'].addRule(Anything,	"OUR",		Anything,	"AOr"	);
		holders['O'-'A'].addRule(Anything,	"OULD",		Anything,	"UHd"	);
		holders['O'-'A'].addRule("^",		"OU",		"^L",		"AH"	);
		holders['O'-'A'].addRule(Anything,	"OUP",		Anything,	"UWp"	);
		holders['O'-'A'].addRule(Anything,	"OU",		Anything,	"AW"	);
		holders['O'-'A'].addRule(Anything,	"OY",		Anything,	"OY"	);
		holders['O'-'A'].addRule(Anything,	"OING",		Anything,	"OWIHNG");
		holders['O'-'A'].addRule(Anything,	"OI",		Anything,	"OY"	);
		holders['O'-'A'].addRule(Anything,	"OOR",		Anything,	"AOr"	);
		holders['O'-'A'].addRule(Anything,	"OOK",		Anything,	"UHk"	);
		holders['O'-'A'].addRule(Anything,	"OOD",		Anything,	"UHd"	);
		holders['O'-'A'].addRule(Anything,	"OO",		Anything,	"UW"	);
		holders['O'-'A'].addRule(Anything,	"O",		"E",		"OW"	);
		holders['O'-'A'].addRule(Anything,	"O",		Nothing,	"OW"	);
		holders['O'-'A'].addRule(Anything,	"OA",		Anything,	"OW"	);
		holders['O'-'A'].addRule(Nothing,	"ONLY",		Anything,	"OWnlIY");
		holders['O'-'A'].addRule(Nothing,	"ONCE",		Anything,	"wAHns"	);
		holders['O'-'A'].addRule(Anything,	"ON'T",		Anything,	"OWnt"	);
		holders['O'-'A'].addRule("C",		"O",		"N",		"AA"	);
		holders['O'-'A'].addRule(Anything,	"O",		"NG",		"AO"	);
		holders['O'-'A'].addRule(" :^",		"O",		"N",		"AH"	);
		holders['O'-'A'].addRule("I",		"ON",		Anything,	"AXn"	);
		holders['O'-'A'].addRule("#:",		"ON",		Nothing,	"AXn"	);
		holders['O'-'A'].addRule("#^",		"ON",		Anything,	"AXn"	);
		holders['O'-'A'].addRule(Anything,	"O",		"ST ",		"OW"	);
		holders['O'-'A'].addRule(Anything,	"OF",		"^",		"AOf"	);
		holders['O'-'A'].addRule(Anything,	"OTHER",	Anything,	"AHDHER");
		holders['O'-'A'].addRule(Anything,	"OSS",		Nothing,	"AOs"	);
		holders['O'-'A'].addRule("#:^",		"OM",		Anything,	"AHm"	);
		holders['O'-'A'].addRule(Anything,	"O",		Anything,	"AA"	);
		
		holders['P'-'A'].addRule(Anything,	"PH",		Anything,	"f"	);
		holders['P'-'A'].addRule(Anything,	"PEOP",		Anything,	"pIYp"	);
		holders['P'-'A'].addRule(Anything,	"POW",		Anything,	"pAW"	);
		holders['P'-'A'].addRule(Anything,	"PUT",		Nothing,	"pUHt"	);
		holders['P'-'A'].addRule(Anything,	"P",		Anything,	"p"	);
		
		holders['Q'-'A'].addRule(Anything,	"QUAR",		Anything,	"kwAOr"	);
		holders['Q'-'A'].addRule(Anything,	"QU",		Anything,	"kw"	);
		holders['Q'-'A'].addRule(Anything,	"Q",		Anything,	"k"	);
		
		holders['R'-'A'].addRule(Nothing,	"RE",		"^#",		"rIY"	);
		holders['R'-'A'].addRule(Anything,	"R",		Anything,	"r"	);
		
		holders['S'-'A'].addRule(Anything,	"SH",		Anything,	"SH"	);
		holders['S'-'A'].addRule("#",		"SION",		Anything,	"ZHAXn"	);
		holders['S'-'A'].addRule(Anything,	"SOME",		Anything,	"sAHm"	);
		holders['S'-'A'].addRule("#",		"SUR",		"#",		"ZHER"	);
		holders['S'-'A'].addRule(Anything,	"SUR",		"#",		"SHER"	);
		holders['S'-'A'].addRule("#",		"SU",		"#",		"ZHUW"	);
		holders['S'-'A'].addRule("#",		"SSU",		"#",		"SHUW"	);
		holders['S'-'A'].addRule("#",		"SED",		Nothing,	"zd"	);
		holders['S'-'A'].addRule("#",		"S",		"#",		"z"	);
		holders['S'-'A'].addRule(Anything,	"SAID",		Anything,	"sEHd"	);
		holders['S'-'A'].addRule("^",		"SION",		Anything,	"SHAXn"	);
		holders['S'-'A'].addRule(Anything,	"S",		"S",		Silent	);
		holders['S'-'A'].addRule(".",		"S",		Nothing,	"z"	);
		holders['S'-'A'].addRule("#:.E",	"S",		Nothing,	"z"	);
		holders['S'-'A'].addRule("#:^##",	"S",		Nothing,	"z"	);
		holders['S'-'A'].addRule("#:^#",	"S",		Nothing,	"s"	);
		holders['S'-'A'].addRule("U",		"S",		Nothing,	"s"	);
		holders['S'-'A'].addRule(" :#",		"S",		Nothing,	"z"	);
		holders['S'-'A'].addRule(Nothing,	"SCH",		Anything,	"sk"	);
		holders['S'-'A'].addRule(Anything,	"S",		"C+",		Silent	);
		holders['S'-'A'].addRule("#",		"SM",		Anything,	"zm"	);
		holders['S'-'A'].addRule("#",		"SN",		"'",		"zAXn"	);
		holders['S'-'A'].addRule(Anything,	"S",		Anything,	"s"	);
		
		holders['T'-'A'].addRule(Nothing,	"THE",		Nothing,	"DHAX"	);
		holders['T'-'A'].addRule(Anything,	"TO",		Nothing,	"tUW"	);
		holders['T'-'A'].addRule(Anything,	"THAT",		Nothing,	"DHAEt"	);
		holders['T'-'A'].addRule(Nothing,	"THIS",		Nothing,	"DHIHs"	);
		holders['T'-'A'].addRule(Nothing,	"THEY",		Anything,	"DHEY"	);
		holders['T'-'A'].addRule(Nothing,	"THERE",	Anything,	"DHEHr"	);
		holders['T'-'A'].addRule(Anything,	"THER",		Anything,	"DHER"	);
		holders['T'-'A'].addRule(Anything,	"THEIR",	Anything,	"DHEHr"	);
		holders['T'-'A'].addRule(Nothing,	"THAN",		Nothing,	"DHAEn"	);
		holders['T'-'A'].addRule(Nothing,	"THEM",		Nothing,	"DHEHm"	);
		holders['T'-'A'].addRule(Anything,	"THESE",	Nothing,	"DHIYz"	);
		holders['T'-'A'].addRule(Nothing,	"THEN",		Anything,	"DHEHn"	);
		holders['T'-'A'].addRule(Anything,	"THROUGH",	Anything,	"THrUW"	);
		holders['T'-'A'].addRule(Anything,	"THOSE",	Anything,	"DHOWz"	);
		holders['T'-'A'].addRule(Anything,	"THOUGH",	Nothing,	"DHOW"	);
		holders['T'-'A'].addRule(Nothing,	"THUS",		Anything,	"DHAHs"	);
		holders['T'-'A'].addRule(Anything,	"TH",		Anything,	"TH"	);
		holders['T'-'A'].addRule("#:",		"TED",		Nothing,	"tIHd"	);
		holders['T'-'A'].addRule("S",		"TI",		"#N",		"CH"	);
		holders['T'-'A'].addRule(Anything,	"TI",		"O",		"SH"	);
		holders['T'-'A'].addRule(Anything,	"TI",		"A",		"SH"	);
		holders['T'-'A'].addRule(Anything,	"TIEN",		Anything,	"SHAXn"	);
		holders['T'-'A'].addRule(Anything,	"TUR",		"#",		"CHER"	);
		holders['T'-'A'].addRule(Anything,	"TU",		"A",		"CHUW"	);
		holders['T'-'A'].addRule(Nothing,	"TWO",		Anything,	"tUW"	);
		holders['T'-'A'].addRule(Anything,	"T",		Anything,	"t"	);
		
		holders['U'-'A'].addRule(Nothing,	"UN",		"I",		"yUWn"	);
		holders['U'-'A'].addRule(Nothing,	"UN",		Anything,	"AHn"	);
		holders['U'-'A'].addRule(Nothing,	"UPON",		Anything,	"AXpAOn");
		holders['U'-'A'].addRule("T",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("S",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("R",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("D",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("L",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("Z",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("N",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("J",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("TH",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("CH",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule("SH",		"UR",		"#",		"UHr"	);
		holders['U'-'A'].addRule(Anything,	"UR",		"#",		"yUHr"	);
		holders['U'-'A'].addRule(Anything,	"UR",		Anything,	"ER"	);
		holders['U'-'A'].addRule(Anything,	"U",		"^ ",		"AH"	);
		holders['U'-'A'].addRule(Anything,	"U",		"^^",		"AH"	);
		holders['U'-'A'].addRule(Anything,	"UY",		Anything,	"AY"	);
		holders['U'-'A'].addRule(" G",		"U",		"#",		Silent	);
		holders['U'-'A'].addRule("G",		"U",		"%",		Silent	);
		holders['U'-'A'].addRule("G",		"U",		"#",		"w"	);
		holders['U'-'A'].addRule("#N",		"U",		Anything,	"yUW"	);
		holders['U'-'A'].addRule("T",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("S",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("R",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("D",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("L",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("Z",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("N",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("J",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("TH",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("CH",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule("SH",		"U",		Anything,	"UW"	);
		holders['U'-'A'].addRule(Anything,	"U",		Anything,	"yUW"	);
		
		holders['V'-'A'].addRule(Anything,	"VIEW",		Anything,	"vyUW"	);
		holders['V'-'A'].addRule(Anything,	"V",		Anything,	"v"	);
		
		holders['W'-'A'].addRule(Nothing,	"WERE",		Anything,	"wER"	);
		holders['W'-'A'].addRule(Anything,	"WA",		"S",		"wAA"	);
		holders['W'-'A'].addRule(Anything,	"WA",		"T",		"wAA"	);
		holders['W'-'A'].addRule(Anything,	"WHERE",	Anything,	"WHEHr"	);
		holders['W'-'A'].addRule(Anything,	"WHAT",		Anything,	"WHAAt"	);
		holders['W'-'A'].addRule(Anything,	"WHOL",		Anything,	"hOWl"	);
		holders['W'-'A'].addRule(Anything,	"WHO",		Anything,	"hUW"	);
		holders['W'-'A'].addRule(Anything,	"WH",		Anything,	"WH"	);
		holders['W'-'A'].addRule(Anything,	"WAR",		Anything,	"wAOr"	);
		holders['W'-'A'].addRule(Anything,	"WOR",		"^",		"wER"	);
		holders['W'-'A'].addRule(Anything,	"WR",		Anything,	"r"	);
		holders['W'-'A'].addRule(Anything,	"W",		Anything,	"w"	);
		
		holders['X'-'A'].addRule(Anything,	"X",		Anything,	"ks"	);
		
		holders['Y'-'A'].addRule(Anything,	"YOUNG",	Anything,	"yAHNG"	);
		holders['Y'-'A'].addRule(Nothing,	"YOU",		Anything,	"yUW"	);
		holders['Y'-'A'].addRule(Nothing,	"YES",		Anything,	"yEHs"	);
		holders['Y'-'A'].addRule(Nothing,	"Y",		Anything,	"y"	);
		holders['Y'-'A'].addRule("#:^",		"Y",		Nothing,	"IY"	);
		holders['Y'-'A'].addRule("#:^",		"Y",		"I",		"IY"	);
		holders['Y'-'A'].addRule(" :",		"Y",		Nothing,	"AY"	);
		holders['Y'-'A'].addRule(" :",		"Y",		"#",		"AY"	);
		holders['Y'-'A'].addRule(" :",		"Y",		"^+:#",		"IH"	);
		holders['Y'-'A'].addRule(" :",		"Y",		"^#",		"AY"	);
		holders['Y'-'A'].addRule(Anything,	"Y",		Anything,	"IH"	);

		holders['Z'-'A'].addRule(Anything,	"Z",		Anything,	"z"	);
		
		holders[26].addRule(Anything,	" ",		Anything,	String.valueOf(Transcriptor.WORD_BREAK)	);
		holders[26].addRule(Anything,	"-",		Anything,	String.valueOf(Transcriptor.WORD_BREAK)	);
		holders[26].addRule(".",		"'S",		Anything,	"z"	);
		holders[26].addRule("#:.E",	"'S",		Anything,	"z"	);
		holders[26].addRule("#",		"'S",		Anything,	"z"	);
		holders[26].addRule(Anything,	"'",		Anything,	Silent	);
		holders[26].addRule(Anything,	",",		Anything,	Silent	);
		holders[26].addRule(Anything,	".",		Anything,	Silent	);
		holders[26].addRule(Anything,	"?",		Anything,	Silent	);
		holders[26].addRule(Anything,	"!",		Anything,	Silent	);
		holders[26].addRule(Anything,	"",		Anything,	Silent	);
	}
	
	public String transcribe(String word) {
		if (word.length()<1) {
			return word;
		}
		
		String retString = "";
		String tString = "";
		
		if (allAlpha(word.trim())) {		
			if (word.charAt(0)!=' ') {
				word = " " + word;
			}
			if (word.charAt(word.length()-1)!=' ') {
				word = word + " ";
			}
			tString = transRule(word);
			
			//change format to match dictionary output
			int pStart = 0;
			int pEnd = 1;
			String phone; 
			while (pEnd <= tString.length()) {
				phone = tString.substring(pStart,pEnd);
				if (phone.charAt(0)>='A' && phone.charAt(0)<='Z') {
					//upcase means two letters
					pEnd++;
					phone = tString.substring(pStart,pEnd);
				}
				
				//translate
				if (phone.equals("WH")) {
					retString = retString + " " + "W";
				} else if (phone.equals("h")) {
					retString = retString + " " + "HH";
				} else if (phone.equals("j")) {
					retString = retString + " " + "JH";
				} else if (phone.equals("AX")) {
					retString = retString + " " + "AH0";
				} else {
					retString = retString + " " + phone.toUpperCase();
				}
				
				pStart = pEnd;
				pEnd = pStart + 1;
			}
		} else {
			retString = transAlphaNum(word.trim());
		}
		
		
		return cleanSpaces(retString);
	}
	
	public boolean allAlpha(String text) {
		for (int i=0; i<text.length(); i++) {
			char c = text.charAt(i);
			if (!(c>='a' && c<='z')) {
				if (!(c>='A' && c<='Z')) {
					if (c!='-' && c!='\'') {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public String transRule(String word) {
		String ret = "";
		
		int remainder = 1;
		while (remainder<word.length()-1) {
			RuleHolder rh;
			char c = word.toUpperCase().charAt(remainder);
			if (c==' ') {
				c = word.toUpperCase().charAt(remainder+1);
			}
			if (c<'A' || c>'Z') {
				rh = holders[26];
			} else {
				rh = holders[c-'A'];
			}
			
			boolean matched = false;
			for (int end=word.length()-1; end>remainder; end--) {
				Rule r = rh.getRule(word.substring(0,remainder), word.substring(remainder,end), word.substring(end));
				if (r!=null) {
					ret = ret + r.getPhoneme();
					remainder += r.getMatch().length();
					matched = true;
					break;
				}
			}
			
			if (!matched) {
				// no match found!
				System.err.println("Error - NO MATCH FOUND FOR CHARACTER " + word.charAt(remainder) + " OF WORD " + word);
				//skip letter
				remainder++;
			}
		}
		
		return ret;
	}
	
	public String transAlphaNum(String text) {
		String ret = "";
		boolean done = false;
		int startIndex = 0;
		int endIndex = 1;
		char c;
		while (!done) {
			c = text.charAt(startIndex);
			if ((c>='A' && c<='Z') || (c>='a' && c<='z')) {
				//say letter
				ret += " " + transLetter(text.substring(startIndex,endIndex));
				startIndex++; 
				endIndex++;
			} else if (c>='0' && c<='9') {
				// extend numerical string
				if (endIndex>=text.length()) {
					ret += Transcriptor.WORD_BREAK + transNumber(Integer.parseInt(text.substring(startIndex)));
					startIndex = endIndex;
				} else if (text.charAt(endIndex)>='0' && text.charAt(endIndex)<='9') {
					endIndex++;
				} else {
					String num = text.substring(startIndex,endIndex);
					ret += Transcriptor.WORD_BREAK + transNumber(Integer.parseInt(num));
					startIndex = endIndex;
					endIndex = startIndex+1;
				}
			} else {
				//gobble up other chars
				startIndex++;
				endIndex = startIndex+1;
			}
			if (startIndex >= text.length()) {
				done = true;
			}
		}
		if (ret.length()>0) {
			// Strip preceding work break
			return ret.substring(1);
		}
		
		return ret;
	}
	
	public String transLetter(String let) {
		char l = let.toUpperCase().charAt(0);
		switch (l) {
		case 'A': return "EY1";
		case 'B': return "B IY1";
		case 'C': return "S IY1";
		case 'D': return "D IY1";
		case 'E': return "IY1";
		case 'F': return "EH1 F";
		case 'G': return "JH IY1";
		case 'H': return "EY1 CH";
		case 'I': return "AY1";
		case 'J': return "JH EY1";
		case 'K': return "K EY1";
		case 'L': return "EH1 L";
		case 'M': return "EH1 M";
		case 'N': return "EH1 N";
		case 'O': return "OW1";
		case 'P': return "P IY1";
		case 'Q': return "K Y UW1";
		case 'R': return "AA1 R";
		case 'S': return "EH1 S";
		case 'T': return "T IY1";
		case 'U': return "Y UW1";
		case 'V': return "V IY1";
		case 'W': return "D AH1 B AH0 L Y UW2";
		case 'X': return "EH1 K S";
		case 'Y': return "W AY1";
		case 'Z': return "Z IY1";
		}
		return "";
	}
	
	public String transNumber(int val) {
		String ret = "";
		if (val<0) {
			ret = "M AY1 N AH0 S " + Transcriptor.WORD_BREAK;
			val = 0 - val;
		}
		
		if (val >= 1000000000)	/* Billions */ {
			ret = ret + transNumber(val/1000000000) + Transcriptor.WORD_BREAK + " B IH1 L Y AH0 N";
			val = val % 1000000000;
			if (val == 0)
				return ret;
			if (val < 100)	 {/* as in THREE BILLION AND FIVE */
				ret += Transcriptor.WORD_BREAK + " AE2 N D ";
			}
			ret += Transcriptor.WORD_BREAK;
		}

		if (val >= 1000000)	/* Millions */ {
			ret = ret + " " + transNumber(val/1000000) + Transcriptor.WORD_BREAK + " M IH1 L Y AH0 N";
			val = val % 1000000;
			if (val == 0)
				return ret;		/* Even million */
			if (val < 100) {	/* as in THREE MILLION AND FIVE */
				ret += Transcriptor.WORD_BREAK + " AE2 N D ";
			}
			ret += Transcriptor.WORD_BREAK;
		}
	
		/* Thousands 1000..1099 2000..99999 */
		/* 1100 to 1999 is eleven-hunderd to ninteen-hunderd */
		if ((val >= 1000 && val <= 1099) || val >= 2000) {
			ret = ret + " " + transNumber(val/1000) + Transcriptor.WORD_BREAK + " TH AW1 Z AH0 N D";
			val = val % 1000;
			if (val == 0)
				return ret;		/* Even thousand */
			if (val < 100) {	/* as in THREE THOUSAND AND FIVE */
				ret += Transcriptor.WORD_BREAK + " AE2 N D ";
			}
			ret += Transcriptor.WORD_BREAK;
		}
	
		if (val >= 100) {
			ret += " " + Cardinals[val/100] + Transcriptor.WORD_BREAK + " HH AH1 N D R AH0 D";
			val = val % 100;
			if (val == 0) {
				return ret;		/* Even hundred */
			}
			ret += Transcriptor.WORD_BREAK;
		}
	
		if (val >= 20) {
			ret += " " + Twenties[(val-20)/10];
			val = val % 10;
			if (val == 0) {
				return ret;		/* Even ten */
			}
			ret += Transcriptor.WORD_BREAK;
		}
	
		ret += Cardinals[val];
		
		return ret.trim();
	}
	
	public static String cleanSpaces(String text) {
		String ret = "";
		boolean prev = false;
		for (int i=0; i<text.length(); i++) {
			if (text.charAt(i)==' ') {
				if (!prev) {
					ret += text.charAt(i);
					prev = true;
				}
			} else {
				ret += text.charAt(i);
				prev = false;
			}
		}
		return ret.trim();
	}
	
}
