@AnalyzerDef(
        name = "noStopWords",
        tokenizer = @TokenizerDef(
                factory = StandardTokenizerFactory.class
        ),
        filters = {
                @TokenFilterDef(
                        factory = StandardFilterFactory.class
                ),
                @TokenFilterDef(
                        factory = LowerCaseFilterFactory.class
                ),
                @TokenFilterDef(
                        factory = SnowballPorterFilterFactory.class,
                        params = @Parameter(name = "language", value = "English")
                ),
                @TokenFilterDef(
                        factory = ASCIIFoldingFilterFactory.class
                )
        },
        charFilters = @CharFilterDef(
                factory = MappingCharFilterFactory.class,
                params = @Parameter(name = "mapping", value = "mapping.txt")
        )
)
package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;