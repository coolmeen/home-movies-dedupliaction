package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.homeass.deduplication.CsvWriter;
import com.homeass.deduplication.movies.MatchingMoviesPair;
import com.homeass.deduplication.movies.Movie;
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
public class CsvReaderIntegrationTest {

    private static final String headers = "id\tyear\tlength\tgenre\tdirectors\tactors\n";


    @Autowired
    CsvReader<Movie> movieCsvReader;


    @Autowired
    CsvWriter<MatchingMoviesPair> moviesPairCsvWriter;

    @Nested
    class reader {

        @Nested
        class validInputs {
            @Test
            @DisplayName("when valid small input should parse")
            public void whenValidInputShouldParse() throws IOException {
                Movie init = init();
                String movie = "tt2355936\t2013\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                List<Movie> results = movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes()));
                Assertions.assertTrue(results.contains(init));
            }

            @Test
            @DisplayName("when empty set should parse")
            public void whenEmptySetShouldParse() throws IOException {
                Assert.isTrue(movieCsvReader.read(new ByteArrayInputStream(headers.getBytes())).isEmpty(), "should be empty");
            }

            @Test
            @DisplayName("when file with empty row should ignore")
            public void whenEmptySetShouldParse4() throws IOException {
                List<Movie> parse = movieCsvReader.read(new FileInputStream("src/test/resources/test.tsv"));
                Assertions.assertEquals(1, parse.size(), "should contain 1 record");
            }

            @Test
            @DisplayName("when file with empty column should be null")
            public void whenEmptySetShouldParse5() throws IOException {
                Movie init = init();
                String movie = "tt2355936\t2013\t89\tDrama\t\\N\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                List<Movie> results = movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes()));
                Assertions.assertEquals(1, results.size(), "should contain 1 record");
                Assert.isNull(results.get(0).getDirectors(), "should be empty List not null");
            }
        }


        @Nested
        class InvalidInputs {
            @Test
            @DisplayName("when Invvalid type input should faile")
            public void whenInvalidInputShouldParse() throws IOException {
                String movie = "tt2355936\tt4\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                Assertions.assertThrows(InvalidFormatException.class, () -> movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes())));
            }

            @Test
            @DisplayName("when missing type input should fail")
            public void whenInvalidInputShouldParse2() throws IOException {
                String movie = "tt2355936\t\\N\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes())));
            }

            // @Test
            @DisplayName("when missing file  should throw exception")
            public void whenInvalidInputShouldParse3() throws IOException {
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new FileInputStream("")));
            }


        }


    }

    @Nested
    class writer {
        @Test
        @DisplayName("when valid small input should parse")
        public void whenValidInputShouldParse() throws IOException {
            Movie init = init();
            List<MatchingMoviesPair> serialize = Collections.singletonList(new MatchingMoviesPair(init, init));
            File outPutFile = new File("file");
            outPutFile.createNewFile();

            File out = moviesPairCsvWriter.write(outPutFile, serialize);

            byte[] bytes = new byte[100];
            new FileInputStream(out).read(bytes);
            String result = new String(bytes);
            Assertions.assertEquals(init.getId() + "\\t" + init.getId(),result);
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