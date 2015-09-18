package com.tb.ba.extraction.parsing;

import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.extraction.XMLParser;
import com.tb.ba.extraction.methods.ExtractionMethod;
import com.tb.ba.extraction.writer.StdTupleElement;

import java.util.List;

/**
 * Diese Klasse behandelt die zu verarbeitenden Artikel aus der Queue Extractor.parseQueue, indem sie die
 * verschiedenen Extraktoren aufruft und ihr den Artikel übergibt.
 * Created by Thimo on 02.07.2015.
 */
public class ParserThread implements Runnable{

    @Override
    public void run() {
        int i = 0;
        while(true){
            i++;
            if(i%100000 == 0){
                System.out.println("ParserThreadAlive!");
            }
            try {
                if(!Extractor.parseQueue.isEmpty()){
                    Extractor.extracting = true;
                    String xml = null;
                    xml = (String) Extractor.parseQueue.poll();

                    Article article = new XMLParser(xml).getArticle();

                    if(article != null){
                        if(article.getNamspace() == 0) {
                            for (ExtractionMethod method : Extractor.methods) {
                                method.extract(article);
                                method.writeElements();
                            }
                        }
                    }





                }else{
                    Extractor.extracting = false;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        if(Extractor.DEBUG){
                            System.out.println("ParserThread");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e){
                if(Extractor.DEBUG){
                    System.out.println("Error: ParserThread: IndexOutOfBoundsException" + e.getStackTrace());
                    e.printStackTrace();
                }
            } catch (Exception e){
                if(Extractor.DEBUG){
                    System.out.println("ParserThread");
                    e.printStackTrace();
                }
            }

        }

    }
}
