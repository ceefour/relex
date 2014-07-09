/*
 * Copyright 2009 Linas Vepstas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package relex.test;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import junitparams.JUnitParamsRunner;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import relex.ParsedSentence;
import relex.RelationExtractor;
import relex.Sentence;
import relex.output.SimpleView;

@RunWith(JUnitParamsRunner.class)
public class RelExTest {
	
	private static RelationExtractor re;
	private static int pass;
	private static int fail;
	private static int subpass;
	private static int subfail;
	private static ArrayList<String> sentfail= new ArrayList<String>();
	
	@BeforeClass
	public static void setUpClass()
	{
		System.err.println("Initializing RelEx...");
		re = new RelationExtractor();
	}

	public static class ComparativesDataProvider {
		
		public static Object[] provideGeneralComparatives() {
			return $(
					$("Some people like pigs less than dogs.",
						"_advmod(like, less)\n" +
		                "_obj(like, pig)\n" +
		                "_quantity(people, some)\n" +
		                "_subj(like, people)\n" +
		                "than(pig, dog)\n"),
					$("Some people like pigs more than dogs.",
		                "_advmod(like, more)\n" +
		                "_obj(like, pig)\n" +
		                "_quantity(people, some)\n" +
		                "_subj(like, people)\n" +
		                "than(pig, dog)\n") );
		}
		
		public static Object[] provideNonEqualGradableComparatives() {
			return $(
            	//Non-equal Gradable : Two entities one feature "more/less"
                $("He is more intelligent than John.",
            	    "than(he, John)\n" +
            	    "_comparative(intelligent, he)\n" +
            	    "degree(intelligent, comparative)\n"+
            	    "_predadj(he, intelligent)\n"),
                $("He is less intelligent than John.",
            	    "than(he, John)\n" +
            	    "_comparative(intelligent, he)\n" +
            	    "degree(intelligent, comparative)\n"+
            	    "_advmod(intelligent, less)\n"+
            	    "_predadj(he, intelligent)\n"),
        	    $("He runs more quickly than John.",
            	    "_advmod(run, quickly)\n" +
            	    "_subj(run, he)\n" +
            	    "than(he, John)\n" +
            	    "_comparative(quickly, run)\n" +
            	    "degree(quickly, comparative)\n"),
        	    $("He runs less quickly than John.",
            	    "_advmod(run, quickly)\n" +
            	    "_subj(run, he)\n" +
            	    "_advmod(quickly, less)\n"+
            	    "than(he, John)\n" +
            	    "_comparative(quickly, run)\n" +
            	    "degree(quickly, comparative)\n"),
                $("He runs more quickly than John does.",
            	    "_advmod(run, quickly)\n" +
            	    "_subj(run, he)\n" +
            	    "_subj(do, John)\n"+
            	    "than(he, John)\n" +
            	    "_comparative(quickly, run)\n" +
            	    "degree(quickly, comparative)\n") );
		}
            	    
		public static Object[] provideUngrammaticalButCommonlyUsedByNonNativeEnglishSpeakersComparatives() {
			return $(
                //This sentence is ungrammatical but commonly used by non-native English speakers 
                $("He runs less quickly than John does.",
            	    "_advmod(run, quickly)\n" +
            	    "_subj(run, he)\n" +
            	    "_subj(do, John)\n"+
            	    "_advmod(quickly, less)\n"+
            	    "than(he, John)\n" +
            	    "_comparative(quickly, run)\n" +
            	    "degree(quickly, comparative)\n"),
    	    	$("He runs slower than John does.",
            	    "_advmod(run, slow)\n" +
            	    "_subj(run, he)\n" +
            	    "_subj(do, John)\n"+
            	    "than(he, John)\n" +
            	    "_comparative(slow, run)\n" +
            	    "degree(slow, comparative)\n"),
                $("He runs more than John.",
            	    "_obj(run, more)\n" +
            	    "_subj(run, he)\n" +
            	    "than(he, John)\n"+
            	    "_comparative(more, run)\n"+
            	    "degree(more, comparative)\n"),
                $("He runs less than John.",
            	    "_obj(run, less)\n" +
            	    "_subj(run, he)\n" +
            	    "than(he, John)\n"+
            	    "_comparative(less, run)\n"+
            	    "degree(less, comparative)\n"),
                $("He runs faster than John.",
                	    "than(he, John)\n" +
                	    "_comparative(fast, run)\n" +
                	    "_subj(run, he)\n"+
                	    "_advmod(run, fast)\n"+
                	    "degree(fast, comparative)\n"),
                $("He runs more slowly than John.",
                	    "than(he, John)\n" +
                	    "_subj(run, he)\n" +
                	    "_comparative(slowly, run)\n"+
                	    "_advmod(run, slowly)\n"+
                	    "degree(slowly, comparative)\n"),
                $("He runs less slowly than John.",
                	    "than(he, John)\n" +
                	    "_subj(run, he)\n" +
                	    "_comparative(slowly, run)\n"+
                	    "_advmod(run, slowly)\n"+
                	    "_advmod(slowly, less)\n"+
                	    "degree(slowly, comparative)\n"),
                $("He runs more miles than John does.",
                	    "than(he, John)\n" +
                	    "_subj(run, he)\n" +
                	    "_subj(do, John)\n"+
                	    "_obj(run, mile)\n"+
                	    "_comparative(mile, run)\n"+
                	    "_quantity(mile, more)\n"+
                	    "degree(more, comparative)\n"),
                $("He runs less miles than John does.",
                	    "than(he, John)\n" +
                	    "_subj(run, he)\n" +
                	    "_subj(do, John)\n"+
                	    "_obj(run, mile)\n"+
                	    "_comparative(mile, run)\n"+
                	    "_quantity(mile, less)\n"+
                	    "degree(less, comparative)\n"),
                $("He runs many more miles than John does.",
                	    "than(he, John)\n" +
                	    "_comparative(mile, run)\n"+
                	    "_obj(run, mile)\n"+
                	    "_subj(run, he)\n" +
                	    "_subj(do, John)\n" +
                	    "_quantity(mile, many)\n"+
                	    "degree(more, comparative)\n"),
                $("He runs fewer miles than John does.",
                	    "than(he, John)\n" +
                	    "_comparative(mile, run)\n"+
                	    "_obj(run, mile)\n"+
                	    "_subj(run, he)\n" +
                	    "_subj(do, John)\n" +
                	    "_quantity(mile, fewer)\n"+
                	    "degree(fewer, comparative)\n"),
                $("He runs ten more miles than John.",
                	    "_obj(run, mile)\n"+
                	    "_subj(run, he)\n" +
                	    "_quantity(mile, more)\n"+
                	    "than(he, John)\n" +
                	    "_comparative(mile, run)\n"+
                	    "_num_quantity(miles, ten)\n" +
                	    "degree(more, comparative)\n"),
                $("He runs almost ten more miles than John does.",
                	    "_obj(run, mile)\n"+
                	    "_subj(run, he)\n"+
                	    "_comparative(mile, run)\n"+
                	    "_subj(do, John)\n"+
                	    "than(he, John)\n"+
                	    "_quantity_mod(ten, almost)\n"+
                	    "_num_quantity(miles, ten)\n"+
                	    "degree(more, comparative)\n"),
                $("He runs more often than John.",
                	    "_subj(run, he)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(he, John)\n"+
                	    "degree(often, comparative)\n"),
                $("He runs less often than John.",
                	    "_subj(run, he)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(he, John)\n"+
                	    "degree(often, comparative)\n"),                				
                $("He runs here more often than John.",
                	    "_advmod(run, here)\n"+
                	    "_subj(run, he)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(he, John)\n"+
                	    "degree(often, comparative)\n"),
                $("He runs here less often than John.",
                	    "_advmod(run, here)\n"+
                	    "_subj(run, he)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(he, John)\n"+
                	    "degree(often, comparative)\n"),
                $("He is faster than John.",
                	    "than(he, John)\n"+
                	    "_predadj(he, fast)\n"+
                	    "_comparative(fast, be)\n"+
                	    "degree(fast, comparative)\n"),
                $("He is faster than John is.",
                	    "than(he, John)\n"+
                	    "_predadj(he, fast)\n"+
                	    "_subj(be, John)\n"+
                	    "_comparative(fast, be)\n"+
                	    "degree(fast, comparative)\n"),
                $("His speed is faster than John's.",
                	    "than(speed, be)\n"+
                	    "_predadj(speed, fast)\n"+
                	    "_poss(speed, him)\n"+
                	    "_comparative(fast, be)\n"+
                	    "degree(fast, comparative)\n"),
                $("I run more than Ben.",
                	    "_subj(run, I)\n"+
                	    "_adv(run, more)\n"+
                	    "_comparative(more, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(more, comparative)\n"),
                $("I run less than Ben.",
                	    "_subj(run, I)\n"+
                	    "_adv(run, less)\n"+
                	    "_comparative(less, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(less, comparative)\n"),
                $("I run more miles than Ben.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, mile)\n"+
                	    "_quantity(mile, more)\n"+
                	    "_comparative(mile, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(more, comparative)\n"),
                $("I run fewer miles than Ben.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, mile)\n"+
                	    "_quantity(mile, fewer)\n"+
                	    "_comparative(mile, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(fewer, comparative)\n"),
                $("I run 10 more miles than Ben.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, mile)\n"+
                	    "_num_quantity(mile, 10)\n"+
                	    "_quantity_mod(10, more)\n"+
                	    "_comparative(mile, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(more, comparative)\n"),
                $("I run 10 fewer miles than Ben.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, mile)\n"+
                	    "_num_quantity(mile, 10)\n"+
                	    "_quantity_mod(10, fewer)\n"+
                	    "_comparative(mile, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(fewer, comparative)\n"),
                $("I run more often than Ben.",
                	    "_subj(run, I)\n"+
                	    "_advmod(run, often)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, more)\n"),
                $("I run less often than Ben.",
                	    "_subj(run, I)\n"+
                	    "_advmod(run, often)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, less)\n"),
                $("I run more often than Ben does.",
                	    "_subj(run, I)\n"+
                	    "_subj(do, Ben)\n"+
                	    "_advmod(run, often)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, more)\n"),
                $("I run less often than Ben does.",
                	    "_subj(run, I)\n"+
                	    "_subj(do, Ben)\n"+
                	    "_advmod(run, often)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, less)\n"),
                $("I run more often than Ben climbs.",
                	    "_subj(run, I)\n"+
                	    "_subj(climb, Ben)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "than1(run, climb)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, more)\n"),
                $("I run less often than Ben climbs.",
                	    "_subj(run, I)\n"+
                	    "_subj(climb, Ben)\n"+
                	    "_comparative(often, run)\n"+
                	    "than(I, Ben)\n"+
                	    "than1(run, climb)\n"+
                	    "degree(often, comparative)\n"+
                	    "_advmod(often, less)\n"),
                $("I run more races than Ben wins contests.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, race)\n"+
                	    "_subj(win, Ben)\n"+
                	    "_obj(win, contest)\n"+
                	    "_quantity(race, more)\n"+
                	    "_comparative(race, run)\n"+
                	    "than(I, Ben)\n"+
                	    "than1(run, climb)\n"+
                	    "than2(race, contest)\n"+
                	    "degree(more, comparative)\n"),
                $("I run fewer races than Ben wins contests.",
                	    "_subj(run, I)\n"+
                	    "_obj(run, race)\n"+
                	    "_subj(win, Ben)\n"+
                	    "_obj(win, contest)\n"+
                	    "_quantity(race, fewer)\n"+
                	    "_comparative(race, run)\n"+
                	    "than(I, Ben)\n"+
                	    "than1(run, climb)\n"+
                	    "than2(race, contest)\n"+
                	    "degree(fewer, comparative)\n"),
                $("I have more chairs than Ben.",
                	    "_obj(have, chair)\n"+
                	    "_subj(have, I)\n"+
                	    "than(I, Ben)\n"+
                	    "_comparative(chair, have)\n"+
                	    "_quantity(chair, more)\n"+
                	    "_advmod(have, more)\n"+
                	    "degree(more, comparative)\n"),
                $("I have fewer chairs than Ben.",
                	    "_obj(have, chair)\n"+
                	    "_subj(have, I)\n"+
                	    "than(I, Ben)\n"+
                	    "_comparative(chair, have)\n"+
                	    "_quantity(chair, fewer)\n"+
                	    "_advmod(have, fewer)\n"+
                	    "degree(fewer, comparative)\n"),
                $("He earns much more money than I do.",
                	    "_obj(earn, money)\n"+
                	    "_subj(do, I)\n"+
                	    "_subj(earn, he)\n"+
                	    "than(he,I)\n"+
                	    "_comparative(money,earn)\n"+
                	    "_advmod(earn, much)\n"+
                	    "_quantity(money, more)\n"+
                	    "_advmod(more, much)\n"+
                	    "degree(more,comparative)\n"),
                $("He earns much less money than I do.",
                	    "_obj(earn, money)\n"+
                	    "_subj(do, I)\n"+
                	    "_subj(earn, he)\n"+
                	    "than(he, I)\n"+
                	    "_comparative(money, earn)\n"+
                	    "_advmod(earn, much)\n"+
                	    "_quantity(money, less)\n"+
                	    "_advmod(less, much)\n"+
                	    "degree(less, comparative)\n"),
                $("She comes here more often than her husband.",
                	    "_advmod(come, here)\n"+
                	    "_subj(come, she)\n"+
                	    "_poss(husband, her)\n"+
                	    "_comparative(often, come)\n"+
                	    "than(she, husband)\n"+
                	    "degree(often, comparative)\n"),
                $("She comes here less often than her husband.",
                	    "_advmod(come, here)\n"+
                	    "_subj(come, she)\n"+
                	    "_poss(husband, her)\n"+
                	    "_comparative(often, come)\n"+
                	    "than(she, husband)\n"+
                	    "degree(often, comparative)\n"),
                $("Russian grammar is more difficult than English grammar.",
                	    "_comparative(difficult, grammar)\n"+
                	    "than(grammar, grammar)\n"+
                	    "_amod(grammar, Russian)\n"+ //When link-grammar uses A, relex should use _amod it will use A instead of AN; will be  updated in next linkgrammer version
                	    "_predadj(grammar, difficult)\n"+
                	    "_amod(grammar, English)\n"+
                	    "degree(difficult, comparative)\n"),
                $("Russian grammar is less difficult than English grammar.",
                	    "_comparative(difficult, grammar)\n"+
                	    "than(grammar, grammar)\n"+
                	    "_amod(grammar, Russian)\n"+
                	    "_predadj(grammar, difficult)\n"+
                	    "_amod(grammar, English)\n"+
                	    "_advmod(difficult, less)\n"+
                	    "degree(difficult, comparative)\n"),
                $("My sister is much more intelligent than me.",
                	    "_amod(much, intelligent)\n"+
                	    "_predadj(sister, intelligent)\n"+
                	    "_poss(sister, me)\n"+
                	    "than(sister, me)\n"+
                	    "_comparative(intelligent, sister)\n"+
                	    "degree(intelligent, comparative)\n"),
                $("My sister is much less intelligent than me.",
                	    "_amod(much, intelligent)\n"+
                	    "_predadj(sister, intelligent)\n"+
                	    "_poss(sister, me)\n"+
                	    "than(sister, me)\n"+
                	    "_comparative(intelligent, sister)\n"+
                	    "_advmod(intelligent, less)\n"+
                	    "degree(intelligent, comparative)\n"),
                $("I find maths lessons more enjoyable than science lessons.",
                	    "_iobj(find, maths)\n"+
                	    "_obj(find, lesson)\n"+
                	    "_subj(find, I)\n"+
                	    "_amod(lesson, enjoyable)\n"+
                	    "_nn(lesson, science)\n"+
                	    "than(maths, science)\n"+
                	    "_comparative(enjoyable, maths)\n"+
                	    "degree(enjoyable, comparative)\n"),
                $("I find maths lessons less enjoyable than science lessons.",
                	    "_iobj(find, maths)\n"+
                	    "_obj(find, lesson)\n"+
                	    "_subj(find, I)\n"+
                	    "_amod(lesson, enjoyable)\n"+
                	    "_nn(lesson, science)\n"+
                	    "than(maths, science)\n"+
                	    "_comparative(enjoyable, maths)\n"+
                	    "_advmod(enjoyable, less)\n"+
                	    "degree(enjoyable, comparative)\n") );
		}

	}

	public ArrayList<String> split(String a)
	{
		String[] sa = a.split("\n");
		ArrayList<String> saa = new ArrayList<String>();
		for (String s : sa) {
			saa.add(s);
		}
		Collections.sort (saa);
		return saa;
	}

	/**
	 * First argument is the sentence.
	 * Second argument is a list of the relations that RelEx
	 * should be generating.
	 * Return true if RelEx generates the same dependencies
	 * as the second argument.
	 */
	public boolean test_sentence(String sent, String sf)
	{
		re.do_penn_tagging = false;
		re.setMaxParses(1);
		Sentence sntc = re.processSentence(sent);
		ParsedSentence parse = sntc.getParses().get(0);
		String rs = SimpleView.printBinaryRelations(parse);
		String urs = SimpleView.printUnaryRelations(parse);

		ArrayList<String> exp = split(sf);
		ArrayList<String> brgot = split(rs);
		ArrayList<String> urgot = split(urs);
		
		//add number of binary relations from parser-output, to total number of relationships got
		int sizeOfGotRelations= brgot.size();
		//check expected binary and unary relations
		//the below for-loop checks whether all expected binary relations are
		//contained in the parser-binary-relation-output arrayList "brgot".
		//if any unary relations are expected in the output it checks the 
		//parser-unary-relation-output arrayList "urgot" for unary relationships
		for (int i=0; i< exp.size(); i++)
		{	
			if(!brgot.contains(exp.get(i)))
			{
				if(!urgot.contains(exp.get(i)))
				{
					System.err.println("Error: content miscompare:\n" +
						    "\tExpected = " + exp + "\n" +
						    "\tGot Binary Relations = " + brgot + "\n" +
						    "\tGot Unary Relations = " + urgot + "\n" +
						    "\tSentence = " + sent);
					subfail ++;
					fail ++;
					sentfail.add(sent);
					return false;
				}
				//add the unary relation, count to totoal number of binary relations
				sizeOfGotRelations++;
			}
			
		}
		//The size checking of the expected relationships vs output relationships
		//is done here purposefully, to accommodate if there is any unary relationships present 
		//in the expected output(see above for-loop also).
		//However it only checks whether parser-output resulted more relationships(binary+unary) than expected relations
		//If the parser-output resulted less relationships(binary+unary) than expected it would 
		//catch that in the above for-loop
		if (exp.size() < sizeOfGotRelations)
		{
			System.err.println("Error: size miscompare:\n" +
					    "\tExpected = " + exp + "\n" +
					    "\tGot Binary Relations = " + brgot + "\n" +
					    "\tGot Unary Relations = " + urgot + "\n" +
					    "\tSentence = " + sent);
			subfail ++;
			fail ++;
			sentfail.add(sent);
			return false;
		}

		subpass ++;
		pass ++;
		return true;
	}

	@Test
	@junitparams.Parameters(source=ComparativesDataProvider.class)
	public void comparatives(String sentence, String expected) {
		assertTrue(test_sentence(sentence, expected));
	}
	
	public static class ConjunctionDataProvider {
		
		public static Object[] provideConjoined() {
			return $(
				//conjoined verbs
				$("Scientists make observations and ask questions.",
	                     "_obj(make, observation)\n" +
	                     "_obj(ask, question)\n" +
	                     "_subj(make, scientist)\n" +
	                     "_subj(ask, scientist)\n" +
	                     "conj_and(make, ask)\n"),
		            //conjoined nouns              
					$("She is a student and an employee.",
					                     "_obj(be, student)\n" +
					                     "_obj(be, employee)\n" +
					                     "_subj(be, she)\n" +
				    	                     "conj_and(student, employee)\n"),
		            //conjoined adjectives
					$("I hailed a black and white taxi.",
					                     "_obj(hail, taxi)\n" +
					                     "_subj(hail, I)\n" +
					                     "_amod(taxi, black)\n" +
				                                 "_amod(taxi, white)\n" +
				    	                     "conj_and(black, white)\n"),
            //conjoined adverbs
             $("She ran quickly and quietly.",
	                     "_advmod(run, quickly)\n" +
	                     "_advmod(run, quietly)\n" +
	                     "_subj(run, she)\n" +
    	                     "conj_and(quickly, quietly)\n"),
            //adjectival modifiers on conjoined subject          
             $("The big truck and the little car collided.",
	                     "_amod(car, little)\n" +
	                     "_amod(truck, big)\n" +
	                     "_subj(collide, truck)\n" +
                                 "_subj(collide, car)\n" +
    	                     "conj_and(truck, car)\n"),
            //verbs with modifiers
            $( "We ate dinner at home and went to the movies.",
	                     "_obj(eat, dinner)\n" +
	                     "conj_and(eat, go)\n" +
	                     "at(eat, home)\n" +
                                 "_subj(eat, we)\n" +
                                 "to(go, movie)\n" +
    	                     "_subj(go, we)\n"),
            //verb with more modifiers
            $("We ate a late dinner at home and went out to the movies afterwards.",
	                     "_obj(eat, dinner)\n" +
	                     "conj_and(eat, go_out)\n" +
	                     "at(eat, home)\n" +
                                 "_subj(eat, we)\n" +
                                 "to(go_out, movie)\n" +
                                 "_advmod(go_out, afterwards)\n" +
                                 "_subj(go_out, we)\n" +
    	                     "_amod(dinner, late)\n"),
            //conjoined ditransitive verbs 
            $("She baked him a cake and sang him a song.",
	                     "_iobj(sing, him)\n" +
	                     "_obj(sing, song)\n" +
	                     "_subj(sing, she)\n" +
                                 "_iobj(bake, him)\n" +
                                 "_obj(bake, cake)\n" +
                                 "conj_and(bake, sing)\n" +
       	                     "_subj(bake, she)\n"),
            //conjoined adverbs with modifiers
            $("she ran very quickly and extremely quietly.",
	                     "_advmod(run, quickly)\n" +
	                     "_advmod(run, quietly)\n" +
	                     "_subj(run, she)\n" +
                                 "_advmod(quietly, extremely)\n" +
                                 "conj_and(quickly, quietly)\n" +
          	                     "_advmod(quickly, very)\n"), 
           //conjoined adverbs with out modifiers
            $("She handled it quickly and gracefully.",
	                     "_obj(handle, quickly)\n" +
	                     "_obj(handle, gracefully)\n" +
	                     "_advmod(handle, quickly)\n" +
                                 "_advmod(handle, gracefully)\n" +
                                 "_subj(handle, she)\n" +
          	                     "conj_and(quickly, gracefully)\n"), 
           //modifiers on conjoined adjectives
            $("He had very long and very white hair.",
	                     "_obj(have, hair)\n" +
	                     "_subj(have, he)\n" +
	                     "_amod(hair, long)\n" +
                                 "_amod(hair, white)\n" +
                                 "_advmod(white, very)\n" +
                                 "conj_and(long, white)\n" +
          	                     "_advmod(long, very)\n"),
           //adjectival modifiers on conjoined object
            $("The collision was between the little car and the big truck.",
	                     "_pobj(between, car)\n" +
	                     "_pobj(between, truck)\n" +
	                     "_psubj(between, collision)\n" +
                                 "_amod(truck, big)\n" +
                                 "_amod(car, little)\n" +
                                 "conj_and(car, truck)\n"),
            //Names Modifiers  and conjunction
            $("Big Tom and Angry Sue went to the movies.",
	                     "to(go, movie)\n" +
	                     "_subj(go, Big_Tom)\n" +
	                     "_subj(go, Angry_Sue)\n" +
                                 "conj_and(Big_Tom, Angry_Sue)\n") );
		}
	}
					
	@Test
	@junitparams.Parameters(source=ConjunctionDataProvider.class)
    public void conjunction(String sentence, String expected) {
		assertTrue(test_sentence(sentence, expected));
	}
	
	public static class ExtrapositionDataProvider {
		
		public static Object[] provideGeneralExtraposition() {
			return $(
				$("The woman who lives next door is a registered nurse.",
                        "_obj(be, nurse)\n" +
                        "_subj(be, woman)\n" +
                        "_amod(nurse, registered)\n" +
                        "_advmod(live, next_door)\n" +
                        "_subj(live, woman)\n" +
                        "who(woman, live)\n"),
                $("A player who is injured has to leave the field.",
                        "_to-do(have, leave)\n" +
                        "_subj(have, player)\n" +
                        "_obj(leave, field)\n" +
                        "_predadj(player, injured)\n" +
                        "who(player, injured)\n"),
                $("Pizza, which most people love, is not very healthy.",
	                        "_advmod(very, not)\n" +
	                        "_advmod(healthy, very)\n" +
	                        "_obj(love, Pizza)\n" +
	                        "_quantity(people, most)\n" +
	                        "which(Pizza, love)\n" +
	                        "_subj(love, people)\n" +
	                        "_predadj(Pizza, healthy)\n"),
	            $("The restaurant which belongs to my aunt is very famous.",
	                        "_advmod(famous, very)\n" +
	                        "to(belong, aunt)\n" +
	                        "_subj(belong, restaurant)\n" +
	                        "_poss(aunt, me)\n" +
	                        "which(restaurant, belong)\n" +
	                        "_predadj(restaurant, famous)\n"),
	            $("The books which I read in the library were written by Charles Dickens.",
	                        "_obj(write, book)\n" +
	                        "by(write, Charles_Dickens)\n" +
	                        "_obj(read, book)\n" +
	                        "in(read, library)\n" +
	                        "_subj(read, I)\n" +
	                        "which(book, read)\n"),
	            $("This is the book whose author I met in a library.",
	                       "_obj(be, book)\n" +
	                       "_subj(be, this)\n" +
	                       "_obj(meet, author)\n" +
	                       "in(meet, library)\n" +
	                       "_subj(meet, I)\n" +
	                       "whose(book, author)\n"),
	            $("The book that Jack lent me is very boring.",
	                       "_advmod(boring, very)\n" +
	                       "_iobj(lend, book)\n" +
	                       "_obj(lend, me)\n" +
	                       "_subj(lend, Jack)\n" +
	                       "that_adj(book, lend)\n" +
	                       "_predadj(book, boring)\n"),
	            $("They ate a special curry which was recommended by the restaurant’s owner.",
	                       "_obj(eat, curry)\n" +
	                       "_subj(eat, they)\n" +
	                       "_obj(recommend, curry)\n" +
	                       "by(recommend, owner)\n" +
	                       "_poss(owner, restaurant)\n" +
	                       "which(curry, recommend)\n" +
	                       "_amod(curry, special)\n"),
	            $("The dog who Jack said chased me was black.",
	                       "_obj(chase, me)\n" +
	                       "_subj(chase, dog)\n" +
	                       "_subj(say, Jack)\n" +
	                       "_predadj(dog, black)\n" +
	                       "who(dog, chase)\n"),
	            $("Jack, who hosted the party, is my cousin.",
	                       "_obj(be, cousin)\n" +
	                       "_subj(be, Jack)\n" +
	                       "_poss(cousin, me)\n" +
	                       "_obj(host, party)\n" +
	                       "_subj(host, Jack)\n" +
	                       "who(Jack, host)\n"),
	            $("Jack, whose name is in that book, is the student near the window.",
	                       "near(be, window)\n" +
	                       "_obj(be, student)\n" +
	                       "_subj(be, Jack)\n" +
	                       "_pobj(in, book)\n" +
	                       "_psubj(in, name)\n" +
	                       "_det(book, that)\n" +
	                       "whose(Jack, name)\n"),
	            $("Jack stopped the police car that was driving fast.",
	                       "_obj(stop, car)\n" +
	                       "_subj(stop, Jack)\n" +
	                       "_advmod(drive, fast)\n" +
	                       "_subj(drive, car)\n" +
	                       "that_adj(car, drive)\n" +
	                       "_nn(car, police)\n"),
                $("Just before the crossroads, the car was stopped by a traffic sign that stood on the street.",
	                       "_obj(stop, car)\n" +
	                       "by(stop, sign)\n" +
	                       "_advmod(stop, just)\n" +
	                       "on(stand, street)\n" +
	                       "_subj(stand, sign)\n" +
	                       "that_adj(sign, stand)\n" +
	                       "_nn(sign, traffic)\n" +
	                       "before(just, crossroads)\n") );
		}
	}
	
	@Test
	@junitparams.Parameters(source=ExtrapositionDataProvider.class)
	public void extraposition(String sentence, String expected) {
		assertTrue(test_sentence(sentence, expected));
	}

}
