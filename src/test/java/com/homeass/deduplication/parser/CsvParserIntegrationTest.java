package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.homeass.deduplication.movies.Movie;
import com.homeass.deduplication.parser.CsvParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class CsvParserIntegrationTest {

    private static final String headers = "id\tyear\tlength\tgenre\tdirectors\tactors\n";


    @Autowired
    CsvParser<Movie> movieCsvParser;



    @Nested
    class validInputs {
        @Test
        @DisplayName("when valid small input should parse")
        public void whenValidInputShouldParse() throws IOException {
            Movie init = init();
            String movie = "tt2355936\t2013\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
            List<Movie> results = movieCsvParser.parse(new ByteArrayInputStream((headers + movie).getBytes()));
            Assertions.assertTrue(results.contains(init));
        }

        @Test
        @DisplayName("when empty set should parse")
        public void whenEmptySetShouldParse() throws IOException {
            Assert.isTrue(movieCsvParser.parse(new ByteArrayInputStream(headers.getBytes())).isEmpty(), "should be empty");
        }

        @Test
        @DisplayName("when file with empty row should ignore")
        public void whenEmptySetShouldParse4() throws IOException {
            List<Movie> parse = movieCsvParser.parse(new FileInputStream("src/test/resources/test.tsv"));
            Assertions.assertEquals(1,parse.size(),"should contain 1 record");
        }

        @Test
        @DisplayName("when file with empty column should be null")
        public void whenEmptySetShouldParse5() throws IOException {
            Movie init = init();
            String movie = "tt2355936\t2013\t89\tDrama\t\\N\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
            List<Movie> results = movieCsvParser.parse(new ByteArrayInputStream((headers + movie).getBytes()));
            Assertions.assertEquals(1,results.size(),"should contain 1 record");
            Assert.isNull(results.get(0).getDirectors(),"should be empty List not null");
        }
    }


    @Nested
    class InvalidInputs {
        @Test
        @DisplayName("when Invvalid type input should faile")
        public void whenInvalidInputShouldParse() throws IOException {
            String movie = "tt2355936\tt4\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
            Assertions.assertThrows(InvalidFormatException.class,() -> movieCsvParser.parse(new ByteArrayInputStream((headers + movie).getBytes())));
        }

        @Test
        @DisplayName("when missing type input should fail")
        public void whenInvalidInputShouldParse2() throws IOException {
            String movie = "tt2355936\t\\N\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
            Assertions.assertThrows(JsonMappingException.class,() -> movieCsvParser.parse(new ByteArrayInputStream((headers + movie).getBytes())));
        }

     // @Test
        @DisplayName("when missing file  should throw exception")
        public void whenInvalidInputShouldParse3() throws IOException {
            Assertions.assertThrows(JsonMappingException.class,() -> movieCsvParser.parse(new FileInputStream("")));
        }



    }
    private Movie init() {
        return Movie.builder()
                .id("tt2355936")
                .length(89)
                .year(2013)
                .genre(Collections.singletonList("Drama"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }

}