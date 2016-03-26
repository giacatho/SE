
package sky.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.stemmer.PorterStemmer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

/**
 *
 * @author cnaquoc
 */
public class Utils {
    
    static Analyzer analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
    public static Analyzer getDefaultAnalyzer() {
        return analyzer;
    }
    
    static final String[] wordSource = new String[] {"a", "able", "about", "above", "abst", "accordance", "according", "accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects", "after", "afterwards", "again", "against", "ah", "ain’t", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "apparently", "appear", "appreciate", "appropriate", "approximately", "are", "aren", "arent", "aren’t", "arise", "around", "as", "a’s", "aside", "ask", "asking", "associated", "at", "auth", "available", "away", "awfully", "b", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "biol", "both", "brief", "briefly", "but", "by", "c", "ca", "came", "can", "cannot", "cant", "can’t", "cause", "causes", "certain", "certainly", "changes", "clearly", "c’mon", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "couldn’t", "course", "c’s", "currently", "d", "date", "definitely", "described", "despite", "did", "didn’t", "different", "do", "does", "doesn’t", "doing", "done", "don’t", "down", "downwards", "due", "during", "e", "each", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "else", "elsewhere", "end", "ending", "enough", "entirely", "especially", "et", "et-al", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f", "far", "few", "ff", "fifth", "first", "five", "fix", "followed", "following", "follows", "for", "former", "formerly", "forth", "found", "four", "from", "further", "furthermore", "g", "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes", "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn’t", "happens", "hardly", "has", "hasn’t", "have", "haven’t", "having", "he", "hed", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "here’s", "hereupon", "hers", "herself", "hes", "he’s", "hi", "hid", "him", "himself", "his", "hither", "home", "hopefully", "how", "howbeit", "however", "hundred", "i", "id", "i’d", "ie", "if", "ignored", "i’ll", "im", "i’m", "immediate", "immediately", "importance", "important", "in", "inasmuch", "inc", "indeed", "index", "indicate", "indicated", "indicates", "information", "inner", "insofar", "instead", "into", "invention", "inward", "is", "isn’t", "it", "itd", "it’d", "it’ll", "its", "it’s", "itself", "i’ve", "j", "just", "k", "keep", "keeps", "kept", "kg", "km", "know", "known", "knows", "l", "largely", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "let’s", "like", "liked", "likely", "line", "little", "‘ll", "look", "looking", "looks", "ltd", "m", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml", "more", "moreover", "most", "mostly", "mr", "mrs", "much", "mug", "must", "my", "myself", "n", "na", "name", "namely", "nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "novel", "now", "nowhere", "o", "obtain", "obtained", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "omitted", "on", "once", "one", "ones", "only", "onto", "or", "ord", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible", "possibly", "potentially", "pp", "predominantly", "present", "presumably", "previously", "primarily", "probably", "promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd", "re", "readily", "really", "reasonably", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting", "results", "right", "run", "s", "said", "same", "saw", "say", "saying", "says", "sec", "second", "secondly", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "shed", "she’ll", "shes", "should", "shouldn’t", "show", "showed", "shown", "showns", "shows", "significant", "significantly", "similar", "similarly", "since", "six", "slightly", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure", "t", "take", "taken", "taking", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that’ll", "thats", "that’s", "that’ve", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there’ll", "thereof", "therere", "theres", "there’s", "thereto", "thereupon", "there’ve", "these", "they", "theyd", "they’d", "they’ll", "theyre", "they’re", "they’ve", "think", "third", "this", "thorough", "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three", "throug", "through", "throughout", "thru", "thus", "til", "tip", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "ts", "t’s", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "up", "upon", "ups", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value", "various", "‘ve", "very", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "was", "wasnt", "wasn’t", "way", "we", "wed", "we’d", "welcome", "well", "we’ll", "went", "were", "we’re", "werent", "weren’t", "we’ve", "what", "whatever", "what’ll", "whats", "what’s", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "where’s", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who’ll", "whom", "whomever", "whos", "who’s", "whose", "why", "widely", "will", "willing", "wish", "with", "within", "without", "wonder", "wont", "won’t", "words", "world", "would", "wouldnt", "wouldn’t", "www", "x", "y", "yes", "yet", "you", "youd", "you’d", "you’ll", "your", "youre", "you’re", "yours", "yourself", "yourselves", "you’ve", "z", "zero"}; 
    static final Set<String> StopWords = new HashSet<String>(Arrays.asList(wordSource));
    
    static List<String> clean(String value) {
        String[] words = value.split(" ");        

        List<String> lstWords = new ArrayList<String>();        
        for (String word: words) {
            word = word.toLowerCase().replaceAll("[^a-z0-9_]", "");
            if (word.equals("a")) continue;

            if (!StopWords.contains(word)) {
                if (word.length() > 0) lstWords.add(word);
            }
        }

        return lstWords;
    }
    
    static PorterStemmer stemmer = new PorterStemmer();
    static POSTaggerME tagger = null;
    static {        
        InputStream modelIn = null;        
        try {
            modelIn = new FileInputStream("en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            tagger = new POSTaggerME(model);
        }
        catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                  modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
    static List<String> cleanEx(String value) {
        List<String> cleanedWords = clean(value);
        String[] words = new String[cleanedWords.size()];
        words = cleanedWords.toArray(words);
        
        if (tagger != null) {
            String[] tags = tagger.tag(words);            
            
            List<String> results = new ArrayList<String>();
            for (int i=0; i< tags.length; i++) {
                //if (tags[i].startsWith("NN")) results.add(stripEnglishPlural(words[i]));
                results.add(stemmer.stem(words[i]));
            }
            
            return results;
        }
        
        return null;
    }
    
    public static String[] extractNouns(String sentenceWithTags) {
        // Split String into array of Strings whenever there is a tag that starts with "._NN"
        // followed by zero, one or two more letters (like "_NNP", "_NNPS", or "_NNS")
        String[] nouns = sentenceWithTags.split("_NN\\w?\\w?\\b");
        // remove all but last word (which is the noun) in every String in the array
        for(int index = 0; index < nouns.length; index++) {
            nouns[index] = nouns[index].substring(nouns[index].lastIndexOf(" ") + 1)
            // Remove all non-word characters from extracted Nouns
            .replaceAll("[^\\p{L}\\p{Nd}]", "");
        }
        return nouns;
    }
    
    public static String extractOriginal(String sentenceWithTags) {
        return sentenceWithTags.replaceAll("_([A-Z]*)\\b", "");
    }
    
    public static String stripEnglishPlural(String word) { 
        // too small? 
        if ( word.length()<4) { 
        return word; 
        } 
        
        // special cases 
        if (word.endsWith("ves"))
        { 
            return word.substring(0,word.length()-2) + "fe"; 
        }
        
        String newWord=word; 
        if ( word.endsWith("sses") || 
             word.endsWith("xes") || 
             word.endsWith("hes") ) { 
            // remove 'es' 
            newWord = word.substring(0,word.length()-2); 
        } 
        else if ( word.endsWith("ies") ) { 
            // remove 'ies', replace with 'y' 
            newWord = word.substring(0,word.length()-3)+'y'; 
        } 
        else if ( word.endsWith("s") && 
                 !word.endsWith("ss") && 
                 !word.endsWith("is") && 
                 !word.endsWith("us") && 
                 !word.endsWith("pos") //&& !word.endsWith("ses") 
                ) { 
        // remove 's' 
        newWord = word.substring(0,word.length()-1); 
        } 
        return newWord; 
    } 
//    public static String removeStopWords(String textFile) throws Exception {
//        CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
//        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_48, new StringReader(textFile.trim()));
//
//        tokenStream = new StopFilter(Version.LUCENE_48, tokenStream, stopWords);
//        StringBuilder sb = new StringBuilder();
//        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
//        tokenStream.reset();
//        while (tokenStream.incrementToken()) {
//            String term = charTermAttribute.toString();
//            sb.append(term + " ");
//        }
//        return sb.toString();
//    }
}
