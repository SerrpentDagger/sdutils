package utilities;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lipsum
{
	public static final String LIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ultrices interdum fringilla. Integer sed nibh ac ipsum imperdiet aliquam. Sed a sapien iaculis, imperdiet lorem eget, venenatis dolor. Nullam mollis dolor vel metus hendrerit, id tempus lectus hendrerit. Suspendisse potenti. Sed vehicula, purus et vehicula viverra, tellus enim blandit justo, tincidunt tristique diam ipsum suscipit diam. Pellentesque laoreet viverra facilisis. In hendrerit, dolor feugiat posuere malesuada, risus sapien laoreet tellus, ac vehicula velit elit nec massa. Fusce sit amet condimentum elit. Donec libero dui, aliquet nec blandit quis, hendrerit et tortor. Integer ac bibendum neque, in dignissim massa.\n" + 
			"Phasellus vestibulum id mauris nec semper. Aliquam lacinia, lacus non mattis lobortis, ex ex faucibus enim, eu euismod leo ligula id quam. Nulla et dictum augue, in facilisis est. Sed dictum convallis est. Aliquam quis ante condimentum, pulvinar ante id, varius tortor. Nulla dapibus varius sem, molestie mattis lectus malesuada vehicula. Vivamus tincidunt interdum sem, vitae condimentum dui molestie at. Ut lacinia enim non enim volutpat, non faucibus mi viverra. Pellentesque ut gravida urna. Aliquam nisl enim, consequat in porta vitae, consectetur vitae quam. Curabitur vulputate efficitur viverra. Etiam tempor facilisis ex. Integer et vehicula nulla. Curabitur ultricies lacus sit amet pretium ornare.\n" + 
			"Maecenas lacus nisl, tincidunt id aliquam ac, aliquet sit amet orci. Nam non pellentesque dui. Ut libero metus, volutpat ut gravida eu, volutpat eget velit. Maecenas commodo non erat nec sollicitudin. Proin in odio in erat faucibus laoreet eget sed lorem. Suspendisse at orci volutpat, pellentesque est vitae, luctus purus. Ut aliquam, mi non aliquam suscipit, tortor odio convallis ex, a congue nunc sapien accumsan quam. Nulla aliquet mi non nibh luctus, eu posuere dolor blandit. Nam luctus arcu in mauris rhoncus bibendum. Quisque porttitor vestibulum massa, a tempor dolor sagittis eu. Cras faucibus fringilla mi eu rhoncus. Quisque vel commodo ex. Pellentesque pretium nibh in facilisis auctor. Nulla facilisi. Praesent varius enim sit amet arcu feugiat, vitae vestibulum nunc gravida.\n" + 
			"In in auctor ex. Nullam auctor iaculis tellus et consequat. Etiam molestie scelerisque nunc a efficitur. Phasellus placerat mollis dictum. Mauris viverra ex erat, non hendrerit nunc aliquet eu. Donec laoreet libero in ante commodo volutpat. Quisque vel mollis velit, et ultrices libero. Nulla sit amet massa in dui euismod rutrum. Proin semper vel augue sit amet laoreet. Proin mollis ornare nisi, ac luctus ante molestie luctus. Ut et mauris sed elit dignissim dignissim sed sit amet leo. Curabitur efficitur pharetra porta. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec a placerat dolor. Sed quis dignissim metus, vitae egestas velit. Donec dapibus scelerisque velit, vitae sodales tellus.\n" + 
			"Nullam ex tellus, blandit quis quam quis, scelerisque egestas odio. In suscipit nulla eu mauris pellentesque placerat. Proin et dolor nec lacus blandit pellentesque at tempor augue. Nullam efficitur tellus vel lorem fringilla eleifend. Curabitur eleifend pharetra tellus sit amet faucibus. Cras at orci tellus. Proin vitae elementum est. Duis condimentum scelerisque augue, quis consequat turpis porttitor vitae. Donec lacinia mi urna, et scelerisque lectus pellentesque ac. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Aenean vitae enim in neque dictum vehicula a quis orci. Nullam vitae posuere tortor. Sed pharetra aliquet odio eget posuere.\n" + 
			"Integer viverra libero vel consequat lobortis. Donec eu vehicula nibh, ac venenatis orci. Nulla et semper eros. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nunc lectus libero, bibendum id eros a, egestas convallis enim. Sed libero lectus, dignissim sed felis ut, convallis gravida dui. Duis pellentesque est tortor, eget porttitor elit tincidunt et. Suspendisse consectetur facilisis nisl, vel auctor elit dictum rutrum. Maecenas nec sollicitudin lacus, sit amet feugiat leo. Aliquam consequat in lacus eu varius. Sed faucibus venenatis ante, in dapibus tellus dapibus ut. Sed facilisis mauris vitae cursus viverra. Nunc id rutrum turpis. Nulla molestie nisi velit, in iaculis mauris placerat suscipit. Etiam ac posuere orci. Ut lobortis rhoncus neque nec lobortis.\n" + 
			"Proin blandit mollis augue et sollicitudin. Vivamus sit amet nisi sed mi eleifend egestas sit amet sit amet eros. Fusce vehicula, dui sed lacinia blandit, arcu quam euismod nisi, ut scelerisque est arcu in arcu. Phasellus imperdiet consequat bibendum. Sed imperdiet, eros et elementum viverra, odio sem consequat dui, sit amet scelerisque felis elit nec neque. Suspendisse consequat enim vel vulputate auctor. Sed fringilla urna id erat finibus mollis nec ac tortor. Praesent eget auctor ante, in maximus sem. Mauris hendrerit lobortis mauris, eu commodo justo iaculis ac.\n" + 
			"Nunc dapibus mattis lacus non ullamcorper. Nulla ac pellentesque nibh. Maecenas mauris leo, interdum ac urna ac, tempus semper elit. Fusce mi eros, consequat nec tellus a, aliquet vehicula orci. Interdum et malesuada fames ac ante ipsum primis in faucibus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. In eu faucibus nisl, at fringilla quam. Cras consequat sapien non velit consectetur, at efficitur urna malesuada. Vivamus ipsum quam, fringilla at nibh non, posuere semper arcu. Aliquam erat volutpat. Etiam at ultrices augue, eget tincidunt erat. Phasellus iaculis rhoncus elit sit amet facilisis.\n" + 
			"Donec sit amet orci viverra, fermentum ex ac, sagittis odio. Donec finibus orci neque, a consectetur nibh hendrerit consequat. Nam euismod nulla at risus pellentesque, vel aliquam tellus commodo. Cras fringilla erat at ex euismod rutrum. Suspendisse potenti. Vestibulum a elit gravida, pellentesque felis quis, aliquam dui. Donec quis leo consequat, maximus mi sagittis, sagittis risus. Proin gravida feugiat viverra. Donec lacus libero, mattis ac euismod eget, ullamcorper quis enim. Aliquam vitae tempus elit.\n" + 
			"Sed sollicitudin felis magna, quis sagittis orci luctus at. Sed aliquet commodo eros, eu consequat dui ultrices eu. Maecenas consequat, urna vel sollicitudin mollis, risus justo facilisis justo, ac rhoncus mi erat ac magna. Sed mi elit, mollis ac dui sed, elementum suscipit metus. Pellentesque elit arcu, fringilla a dapibus vel, semper ac mi. Pellentesque sollicitudin ex nec magna elementum posuere. Donec a porttitor leo, quis ornare sem.\n" + 
			"Etiam a dolor id urna imperdiet faucibus ut et ante. Maecenas dictum tellus in leo ornare, imperdiet iaculis lorem pulvinar. Donec fermentum viverra nibh at pulvinar. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla luctus interdum lobortis. Etiam at massa dignissim nunc vulputate mattis. Nullam sem tellus, volutpat id posuere non, convallis nec turpis. Aliquam ante purus, finibus et viverra a, bibendum condimentum erat. In scelerisque interdum tellus, eget vestibulum lacus placerat eu. Nulla facilisi. Pellentesque venenatis nulla risus, a laoreet ante maximus at. Cras in aliquet arcu, id vulputate enim. Nullam vitae lorem tellus.\n" + 
			"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed elit eros, scelerisque at ornare non, sollicitudin cursus nibh. Nulla vitae pharetra enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Duis ut metus velit. Cras ac velit est. Curabitur condimentum vestibulum turpis, nec tempus lorem consequat at. Proin placerat posuere felis, id laoreet neque.\n" + 
			"Donec vehicula scelerisque felis vel vulputate. Vivamus porttitor mollis molestie. Sed placerat, enim id aliquet commodo, diam nulla efficitur lectus, id ultricies nulla elit in leo. Nunc mattis sapien eu nisl iaculis ultrices. Curabitur dui quam, sagittis non sem at, tincidunt dapibus ipsum. Cras dignissim eros hendrerit varius lobortis. Curabitur eleifend, erat id tristique blandit, orci odio sagittis nulla, at varius felis magna nec ex.\n" + 
			"Suspendisse et fermentum enim. In lacus libero, maximus sed sagittis sed, aliquet at dolor. Ut purus ante, ornare sit amet fermentum nec, sagittis vitae metus. Sed in sollicitudin augue, sed pulvinar neque. Mauris dapibus ac massa faucibus scelerisque. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec rhoncus sit amet turpis ornare gravida. Morbi feugiat pretium mauris ut commodo.\n" + 
			"Phasellus quis tempus augue. Praesent consequat aliquet aliquet. Suspendisse porta urna vel mauris fringilla pellentesque. Sed vitae orci erat. Curabitur vel aliquam ex, quis vulputate ligula. Ut et sodales lorem. Maecenas in lobortis augue. Sed tincidunt nec neque ut porta. Suspendisse potenti. Vestibulum mollis venenatis risus eget molestie. Aenean dapibus a massa nec rutrum.\n" + 
			"Etiam nec nulla mollis, auctor turpis sed, congue risus. Proin eget leo ac lectus pulvinar porta. Pellentesque pharetra congue eros vel efficitur. Duis consectetur aliquam eros nec sodales. Nulla viverra turpis vitae congue vestibulum. Proin quis rhoncus tortor, sed lacinia dolor. Integer auctor vestibulum tincidunt. Nunc id efficitur libero. Mauris dapibus mauris vel dolor rhoncus ultrices. Nullam maximus rutrum odio vitae euismod. Vivamus tincidunt dui lacinia, vulputate turpis ut, vestibulum mauris.\n" + 
			"Aliquam commodo molestie massa nec suscipit. Nulla facilisi. Curabitur leo dui, aliquam ac aliquam sit amet, pellentesque id neque. Curabitur sagittis neque et posuere tempus. Mauris dignissim laoreet velit, a scelerisque nibh condimentum et. Mauris et feugiat velit, ut pharetra nunc. Cras condimentum felis et nisi lacinia pharetra. Donec pellentesque lobortis lorem, a porta leo sollicitudin tincidunt. Cras iaculis tristique dolor id varius. Fusce pharetra, purus finibus placerat porta, libero lectus interdum elit, sit amet posuere diam nisl a urna. Ut feugiat fermentum arcu a pulvinar.\n" + 
			"Suspendisse mi sem, pellentesque sit amet enim sed, varius elementum eros. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Duis et tempus metus, quis dapibus urna. Aliquam ac aliquam urna. Pellentesque quis arcu vel tellus cursus viverra. Nullam posuere sit amet neque sed scelerisque. Quisque bibendum dui id nisl pulvinar porta. In hac habitasse platea dictumst. Phasellus vitae venenatis magna. Donec at nisl ac nulla varius placerat tempus venenatis tortor. Curabitur vel magna at enim tempus tempus. Morbi a magna sed mauris cursus vestibulum ut gravida sapien. Morbi consectetur eros at lobortis rutrum. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" + 
			"Quisque elementum id metus quis commodo. Fusce blandit eu sem non vulputate. Quisque sed tempor turpis, ut porttitor augue. Suspendisse facilisis ultricies massa quis volutpat. Mauris iaculis semper nunc eu vestibulum. Fusce eget tortor metus. Aenean semper risus eget dolor eleifend commodo. Mauris vitae magna neque. Vivamus gravida vitae leo eu vehicula. Fusce non leo ipsum. Proin fermentum, nisl feugiat consectetur cursus, risus nunc iaculis nulla, vitae mollis ex quam ut dolor. Suspendisse potenti. Sed feugiat laoreet gravida. Maecenas nisl diam, vestibulum molestie placerat ut, laoreet et nisl. Cras laoreet ipsum nunc, ac aliquam eros malesuada ut. Etiam fermentum lorem nec lectus pretium ullamcorper.\n" + 
			"Pellentesque quis pulvinar metus. Aliquam pellentesque cursus odio ac convallis. Sed volutpat dolor ac tortor efficitur varius. Aliquam condimentum feugiat sem eu consequat. Quisque vitae sapien viverra, ultricies lectus vel, euismod tellus. Cras tristique metus at metus efficitur, at venenatis quam lobortis. Etiam vestibulum faucibus dolor, et finibus est ullamcorper non. Cras elementum purus at lobortis malesuada. Nunc semper purus ante, id vestibulum arcu pellentesque ac. Cras nunc lorem, porta sit amet arcu in, sollicitudin semper purus. Suspendisse malesuada blandit massa, et accumsan urna facilisis nec. Fusce lobortis, ante ut tempor laoreet, justo mi lacinia diam, porttitor dignissim augue leo vel ex. Praesent ac risus ornare, dictum dolor id, pulvinar sapien. Mauris justo lacus, finibus finibus lobortis a, commodo sit amet ex.";
	private static String[] PARAGRAPHS = null;
	private static String[] SENTENCES = null;
	private static String[] WORDS = null;
	
	static
	{
		System.out.println("Loading Lipsum Generator");
		
		PARAGRAPHS = LIPSUM.split("\\v");
		
		BreakIterator bi = BreakIterator.getSentenceInstance(Locale.ENGLISH);
		bi.setText(LIPSUM);
		int start = bi.first();
		ArrayList<String> tmp = new ArrayList<String>();
		for (int end = bi.next(); end != BreakIterator.DONE; start = end, end = bi.next())
		    tmp.add(LIPSUM.substring(start, end).trim());
		SENTENCES = tmp.toArray(new String[tmp.size()]);
		
		bi = BreakIterator.getWordInstance(Locale.ENGLISH);
		bi.setText(LIPSUM);
		start = bi.first();
		tmp.clear();
		Pattern empty = Pattern.compile("[\\s\\p{Punct}]");
		Matcher isEmpty;
		for (int end = bi.next(); end != BreakIterator.DONE; start = end, end = bi.next())
		{
			String word = LIPSUM.substring(start, end).trim();
			isEmpty = empty.matcher(word);
			if (!isEmpty.matches() && !word.isEmpty())
				tmp.add(word);
		}
		WORDS = tmp.toArray(new String[tmp.size()]);
		
		System.out.println("Loaded Lipsum Generator");
	}
	
	public static String[] getParagraphs(int count)
	{ return ArrayUtils.randomFetch(PARAGRAPHS, new String[count]); }
	public static String getParagraph()
	{ return getParagraphs(1)[0]; }
	
	public static String[] getSentences(int count)
	{ return ArrayUtils.randomFetch(SENTENCES, new String[count]); }
	public static String getSentence()
	{ return getSentences(1)[0]; }
	
	public static String[] getWords(int count)
	{ return getWords(count, Case.LOWER); }
	public static String getWord()
	{ return getWords(1)[0]; }
	
	public static String[] getWordStrings(int count, int words, Case caze)
	{
		Random ran = new Random();
		String[] out = new String[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = "";
			for (int j = ran.nextInt(WORDS.length - words); j < words; j++)
				out[i] += WORDS[j] + (j == words - 1 ? "" : " ");
		}
		return out;
	}
	public static String getWordString(int words, Case caze)
	{ return getWordStrings(1, words, caze)[0]; }
	
	public static String[] getWords(int count, Case caze)
	{ return ArrayUtils.randomFetch(WORDS, new String[count], caze.process); }
	public static String getWord(Case caze)
	{ return getWords(1, caze)[0]; }
	
	public static enum Case
	{
		UPPER((s) -> s.toUpperCase()), LOWER((s) -> s.toLowerCase()),
		TITLE((s) -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
		
		public final Function<String, String> process;
		Case(Function<String, String> pro)
		{
			process = pro;
		}
	}
}
