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
 * This class will extract authorNames from the given PDF
 *
 * @author Wasif Altaf, Asif Altaf, Ben Litchfield
 */
public class AuthorExtractor extends PDFTextStripper {

    private String fileNamePathWithExtension;
    private LinkedHashMap<String, List<TextPosition>> authorNames;
    private LinkedHashMap<String, List<TextPosition>> authorAffiliations;
    private LinkedHashMap<String, List<TextPosition>> authorContacts;
    private boolean titleStartFlag = false,
            titleEndFlag = false;
    private boolean authorNamesStartFlag = false,
            authorNamesEndFlag = false;
    
    private boolean authorAffiliationsStartFlag = false,
            authorAffiliationsEndFlag = false;
    private String authorAffiliationsFont = null;
    
    private boolean authorContactsStartFlag = false,
            authorContactsEndFlag = false;
    private String authorContactsFont = null;

//    public TitleExtractor(String fileNamePathWithExtension) throws IOException {
//        this.fileNamePathWithExtension = fileNamePathWithExtension;
//        authorNames = new LinkedHashMap<>(30);
//    }
    public AuthorExtractor() throws IOException {
        authorNames = new LinkedHashMap<>();
        authorAffiliations = new LinkedHashMap<>();
        authorContacts = new LinkedHashMap<>();
    }

    private boolean process() throws IOException {
        boolean toReturn = false;
        PDFTextStripper stripper = new AuthorExtractor();
        PDDocument document = null;

        try {
            document = PDDocument.load(new File(this.getFileNamePathWithExtension()));

            //((TitleExtractor) stripper).setFileNamePathWithExtension(this.getFileNamePathWithExtension());
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(1);

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            setAuthorNames(((AuthorExtractor) stripper).getAuthorNames());
            setAuthorAffiliations(((AuthorExtractor) stripper).getAuthorAffiliations());
            setAuthorContacts(((AuthorExtractor) stripper).getAuthorContacts());

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

        // if text size is 14 and authorNames not started yet, then mark start of authorNames
        if (textPositions.get(0).getFontSizeInPt() == 14.0
                && this.titleStartFlag == false) {
            this.titleStartFlag = true;
            // set string into authorNames map
            //authors.put(string, textPositions);

            //System.out.println(getMapAsString(authorNames));
        } else if (textPositions.get(0).getFontSizeInPt() == 14.0 && this.titleStartFlag == true) {
            // set string into authorNames map
            //authors.put(string, textPositions);

            //System.out.println(getMapAsString(authorNames));
        } // if string size is less than
        else if (textPositions.get(0).getFontSizeInPt() < 14.0) {
            // mark the end of authorNames
            this.titleEndFlag = true;
        }

        // extracting author names
        if (titleStartFlag == true && titleEndFlag == true) {
            if (textPositions.get(0).getFontSizeInPt() == 9.0
                    && authorNamesStartFlag == false
                    && authorNamesEndFlag == false) {
                authorNamesStartFlag = true;

                authorNames.put(string, textPositions);

                System.out.println(getMapAsString(authorNames));
            } else if (textPositions.get(0).getFontSizeInPt() == 9.0
                    && authorNamesStartFlag == true
                    && authorNamesEndFlag == false) {
                authorNames.put(string, textPositions);

                System.out.println(getMapAsString(authorNames));
            } else if (textPositions.get(0).getFontSizeInPt() < 9.0
                    && authorNamesStartFlag == true
                    && authorNamesEndFlag == false) {

                authorNamesEndFlag = true;

                System.out.println(getMapAsString(authorNames));
            }

            // extracting author affiliations
            if (authorNamesStartFlag == true && authorNamesEndFlag == true) {
                // mark author affiliations start, font and end
                if (textPositions.get(0).getFontSizeInPt() == 8.0
                        && authorAffiliationsStartFlag == false
                        && authorAffiliationsEndFlag == false) {
                    // mark author affiliations start and remember font
                    authorAffiliationsStartFlag = true;
                    authorAffiliationsFont = textPositions.get(0).getFont().toString();

                    authorAffiliations.put(string, textPositions);

                    System.out.println(getMapAsString(authorAffiliations));
                } else if (textPositions.get(0).getFontSizeInPt() == 8.0
                        && textPositions.get(0).getFont().toString().equalsIgnoreCase(authorAffiliationsFont)
                        && authorAffiliationsStartFlag == true
                        && authorAffiliationsEndFlag == false) {
                    authorAffiliationsStartFlag = true;

                    authorAffiliations.put(string, textPositions);

                    System.out.println(getMapAsString(authorAffiliations));
                } // if the font style changes author contacts might start
                else if (!textPositions.get(0).getFont().toString().equalsIgnoreCase(authorAffiliationsFont)
                        && authorAffiliationsStartFlag == true
                        && authorAffiliationsEndFlag == false) {
                    authorAffiliationsEndFlag = true;

                    System.out.println(getMapAsString(authorAffiliations));
                }
            }
            
            // extracting author contact information
            if (authorAffiliationsStartFlag == true && authorAffiliationsEndFlag == true) {
                // mark author contact start, font
                if (textPositions.get(0).getFontSizeInPt() == 8.0
                        && authorContactsStartFlag == false
                        && authorContactsEndFlag == false) {
                    // mark author contact start and remember font
                    authorContactsStartFlag = true;
                    authorContactsFont = textPositions.get(0).getFont().toString();

                    authorContacts.put(string, textPositions);

                    System.out.println(getMapAsString(authorContacts));
                } else if (textPositions.get(0).getFontSizeInPt() == 8.0
                        && textPositions.get(0).getFont().toString().equalsIgnoreCase(authorContactsFont)
                        && authorContactsStartFlag == true
                        && authorContactsEndFlag == false) {
                    authorContactsStartFlag = true;

                    authorContacts.put(string, textPositions);

                    System.out.println(getMapAsString(authorContacts));
                } // if the font style changes author keywords might start
                else if (!textPositions.get(0).getFont().toString().equalsIgnoreCase(authorContactsFont)
                        && authorContactsStartFlag == true
                        && authorContactsEndFlag == false) {
                    authorContactsEndFlag = true;

                    System.out.println(getMapAsString(authorContacts));
                }
            }
            
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
     * @return the authorNames as String
     */
    public String getAuthorsAsString() throws IOException {
        process();

        return getMapAsString(this.getAuthorNames());
    }
    
    /**
     *
     * @return the authorAffiliations as String
     */
    public String getAuthorsAffiliationsAsString() throws IOException {
        process();

        return getMapAsString(this.getAuthorAffiliations());
    }
    
    /**
     *
     * @return the authorContactsas String
     */
    public String getAuthorsContactsAsString() throws IOException {
        process();

        return getMapAsString(this.getAuthorContacts());
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

    public LinkedHashMap<String, List<TextPosition>> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(LinkedHashMap<String, List<TextPosition>> authorNames) {
        this.authorNames = authorNames;
    }

    public LinkedHashMap<String, List<TextPosition>> getAuthorAffiliations() {
        return authorAffiliations;
    }

    public void setAuthorAffiliations(LinkedHashMap<String, List<TextPosition>> authorAffiliations) {
        this.authorAffiliations = authorAffiliations;
    }

    public LinkedHashMap<String, List<TextPosition>> getAuthorContacts() {
        return authorContacts;
    }

    public void setAuthorContacts(LinkedHashMap<String, List<TextPosition>> authorContacts) {
        this.authorContacts = authorContacts;
    }
}