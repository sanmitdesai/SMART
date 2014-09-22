
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Demonstrates how to first use the tagger, then use the
 * ShiftReduceParser.  Note that ShiftReduceParser will not work
 * on untagged text.
 *
 * @author John Bauer
 */
public class ShiftReduceDemo {
	public static void main(String[] args) {
		String modelPath = "F:/englishSR.ser.gz";
		String taggerPath = "lib\\models\\english-left3words-distsim.tagger";

		//    for (int argIndex = 0; argIndex < args.length; ) {
		//      if (args[argIndex].equals("-tagger")) {
		//        taggerPath = args[argIndex + 1];
		//        argIndex += 2;
		//      } else if (args[argIndex].equals("-model")) {
		//        modelPath = args[argIndex + 1];
		//        argIndex += 2;
		//      } else {
		//        throw new RuntimeException("Unknown argument " + args[argIndex]);
		//      }
		//    }

		String text = "Special force police unit was deployed and used against protesters in #Kiev  #Ukraine during #ukraineprotests";

		MaxentTagger tagger = new MaxentTagger(taggerPath);
		ShiftReduceParser model = ShiftReduceParser.loadModel(modelPath);

		DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
		for (List<HasWord> sentence : tokenizer) {
			List<TaggedWord> tagged = tagger.tagSentence(sentence);
			Tree tree = model.apply(tagged);
//			      System.out.println(tree.getLeaves());
			TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
			Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
//			System.out.println(td);

			Object[] list = td.toArray();
//			System.out.println(list.length);
			TypedDependency typedDependency;
			for (Object object : list) {
				typedDependency = (TypedDependency) object;
				

//					if(typedDependency.dep().nodeString().equals("obama"))
					System.out.println(typedDependency.dep().nodeString()+ " :: "+typedDependency.reln()+" >> "+typedDependency.gov().nodeString());
					
			}
		}
	}
}
