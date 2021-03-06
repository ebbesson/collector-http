/* Copyright 2015 Norconex Inc.
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
package com.norconex.collector.http.pipeline.importer;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.norconex.collector.http.crawler.HttpCrawler;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.data.HttpCrawlData;
import com.norconex.collector.http.doc.HttpDocument;
import com.norconex.commons.lang.io.CachedStreamFactory;

/**
 * @author Pascal Essiembre
 * @since 2.2.0
 */
public class HttpImporterPipelineTest {

    @Test
    public void testCanonicalStageSameReferenceContent() {
        String reference = "http://www.example.com/file.pdf";
        String contentValid = "<html><head><title>Test</title>\n"
                + "<link rel=\"canonical\"\n href=\"\n" + reference +  "\" />\n"
                + "</head><body>Nothing of interest in body</body></html>";
        HttpDocument doc = new HttpDocument(reference, 
                new CachedStreamFactory(1000, 1000).newInputStream(
                        new ByteArrayInputStream(contentValid.getBytes())));
        HttpImporterPipelineContext ctx = new HttpImporterPipelineContext(
                new HttpCrawler(new HttpCrawlerConfig()), 
                null, new HttpCrawlData(reference, 0), null, doc);
        Assert.assertTrue(HttpImporterPipelineUtil.resolveCanonical(
                ctx, false));
    }
    
    @Test
    public void testCanonicalStageSameReferenceHeader() {
        String reference = "http://www.example.com/file.pdf";
        HttpDocument doc = new HttpDocument(reference, 
                new CachedStreamFactory(1, 1).newInputStream());
        doc.getMetadata().setString(
                "Link", "<" + reference + "> rel=\"canonical\"");
        HttpImporterPipelineContext ctx = new HttpImporterPipelineContext(
                new HttpCrawler(new HttpCrawlerConfig()), 
                null, new HttpCrawlData(reference, 0), null, doc);
        Assert.assertTrue(HttpImporterPipelineUtil.resolveCanonical(ctx, true));
    }

}
