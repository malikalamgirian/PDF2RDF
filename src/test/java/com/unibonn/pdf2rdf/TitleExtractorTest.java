/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibonn.pdf2rdf;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.text.TextPosition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wasif Altaf
 */
public class TitleExtractorTest {

    public TitleExtractorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTitleAsString method, of class TitleExtractor.
     */
    @org.junit.Test
    public void testGetTitleAsString() throws IOException {
        System.out.println("getTitleAsString");

        TitleExtractor instance = new TitleExtractor();
        String fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" 
                + "test.pdf";
        String expResult = "Structuring Linked Data Search Results Using Probabilistic Soft Logic";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        String result = instance.getTitleAsString();

        assertEquals(expResult, result);

        instance = new TitleExtractor();
        fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" +
                "splnproc1703.pdf";
        expResult = "Contribution Title";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        result = instance.getTitleAsString();

        assertEquals(expResult, result);

    }
}
