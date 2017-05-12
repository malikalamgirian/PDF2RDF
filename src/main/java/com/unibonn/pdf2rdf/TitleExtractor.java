/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unibonn.pdf2rdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * This class will extract title from the given PDF
 *
 * @author Wasif Altaf, Ben Litchfield
 */
public class TitleExtractor extends PDFTextStripper {

    private String fileNamePathWithExtension;
    private LinkedHashMap<String, List<TextPosition>> title;
    private boolean titleStartFlag = false,
            titleEndFlag = false;

//    public TitleExtractor(String fileNamePathWithExtension) throws IOException {
//        this.fileNamePathWithExtension = fileNamePathWithExtension;
//        title = new LinkedHashMap<>(30);
//    }

    public TitleExtractor() throws IOException {
        title = new LinkedHashMap<>();
    }

    private boolean process() throws IOException {
        boolean toReturn = false;
        PDFTextStripper stripper = new TitleExtractor();
        PDDocument document = null;

        try {
            document = PDDocument.load(new File(this.getFileNamePathWithExtension()));

            //((TitleExtractor) stripper).setFileNamePathWithExtension(this.getFileNamePathWithExtension());
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(1);

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
            
            setTitle(((TitleExtractor) stripper).getTitle());

            toReturn = true;
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return toReturn;
    }

    /**
     *
     * @param string
     * @param textPositions
     * @throws IOException
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {

        // if text size is 14 and title not started yet, then mark start of title
        if (textPositions.get(0).getFontSizeInPt() == 14.0 && this.titleStartFlag == false) {
            this.titleStartFlag = true;
            // set string into title map
            title.put(string, textPositions);

            System.out.println(getMapAsString(title));
        } else if (textPositions.get(0).getFontSizeInPt() == 14.0 && this.titleStartFlag == true) {
            // set string into title map
            title.put(string, textPositions);

            System.out.println(getMapAsString(title));
        } // if string size is less than
        else if (textPositions.get(0).getFontSizeInPt() < 14.0) {
            // mark the end of title
            this.titleEndFlag = true;
        }
         

        for (TextPosition text : textPositions) {
            System.out.println("String[" + text.getXDirAdj() + ", " + text.getYDirAdj()
                    + " fs=" + text.getFontSize()
                    + " xscale=" + text.getXScale()
                    + " height=" + text.getHeightDir()
                    + " space=" + text.getWidthOfSpace()
                    + " width=" + text.getWidthDirAdj()
                    + " font=" + text.getFont().toString()
                    + " fontSizeInPT=" + text.getFontSizeInPt()
                    + "]" + text.getUnicode()
            );
        }
        System.out.println(string);
        System.out.println("\n");
    }

    /**
     *
     * @return the title as String
     */
    public String getTitleAsString() throws IOException {        
        process();        

        return getMapAsString(this.getTitle());
    }

    /**
     * 
     * @param map
     * @return 
     */
    private String getMapAsString(LinkedHashMap<String, List<TextPosition>> map) {
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, List<TextPosition>> entry : map.entrySet()) {
            String word = entry.getKey();
            List<TextPosition> positions = entry.getValue();

            sb.append(word);
            sb.append(" ");
        }

        return sb.toString().trim();
    }

    public String getFileNamePathWithExtension() {
        return fileNamePathWithExtension;
    }

    public void setFileNamePathWithExtension(String fileNamePathWithExtension) {
        this.fileNamePathWithExtension = fileNamePathWithExtension;
    }

    public LinkedHashMap<String, List<TextPosition>> getTitle() {
        return title;
    }

    public void setTitle(LinkedHashMap<String, List<TextPosition>> title) {
        this.title = title;
    }
    
}
