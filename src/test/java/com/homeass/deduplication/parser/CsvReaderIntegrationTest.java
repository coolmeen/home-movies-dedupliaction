package com.homeass.deduplication.parser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.homeass.deduplication.movies.entity.MatchingMovieToCsv;
import com.homeass.deduplication.movies.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class CsvReaderIntegrationTest {

    private static final String headers = "id\tyear\tlength\tgenre\tdirectors\tactors\n";


    @Autowired
    CsvReader<Movie> movieCsvReader;


    @Autowired
    CsvWriter<MatchingMovieToCsv> moviesPairCsvWriter;

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

            @Test
            @DisplayName("when first raw contain headers should ignore")
            public void whenContainHeadersShouldIgnore() throws IOException {
                Movie init = init();
                String movie = "id\tyear\tlength\tgenre\tdirectors\tactors\n" +
                        "tt2355936\t2013\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                List<Movie> results = movieCsvReader.read(new ByteArrayInputStream((movie).getBytes()));
                Assertions.assertEquals(1, results.size(), "should contain 1 record");
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
            @DisplayName("when missing id should fail")
            public void whenInvalidInputShouldFail1() throws IOException {
                String movie = "\\N\t\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes())));
            }
            @Test
            @DisplayName("when missing year should fail")
            public void whenInvalidInputShouldFail2() throws IOException {
                String movie = "tt2355936\t\\N\t89\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes())));
            }

            @Test
            @DisplayName("when missing length should fail")
            public void whenInvalidInputShouldFail3() throws IOException {
                String movie = "tt2355936\t1989\\N\tDrama\tLina Chamie\tDavi Galdeano,Gregório Mussatti Cesare,Dira Paes,Julia Weiss,Antônia Ricca,Marco Ricca,Lucas Zamberlan";
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new ByteArrayInputStream((headers + movie).getBytes())));
            }

            // @Test
            @DisplayName("when missing file  should throw JsonMappingException exception")
            public void whenInvalidInputShouldParse3() throws IOException {
                Assertions.assertThrows(JsonMappingException.class, () -> movieCsvReader.read(new FileInputStream("")));
            }

        }


    }

    @Nested
    class writer {
        @Test
        @DisplayName("when valid small input should write and seprator is tab")
        public void whenValidInputShouldParseAndSeparatorIsTab() throws IOException {
            Movie movie = init();
            List<MatchingMovieToCsv> serialize = Collections.singletonList(new MatchingMovieToCsv(Arrays.asList(movie.getId(), movie.getId())));
            File file = new File("file");
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStream out = moviesPairCsvWriter.write(fileOutputStream, serialize);

            Assertions.assertEquals(movie.getId() + "\t" + movie.getId() +"\n", new String(Files.readAllBytes(Paths.get("file"))));
        }

        @Test
        @DisplayName("when valid input should not contain headers")
        public void whenValidInputShouldNoHeaders() throws IOException {
            Movie movie = init();
            List<MatchingMovieToCsv> serialize = Collections.singletonList(new MatchingMovieToCsv(Arrays.asList(movie.getId(), movie.getId())));
            File file = new File("file");
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStream out = moviesPairCsvWriter.write(fileOutputStream, serialize);

            Assertions.assertEquals(movie.getId() + "\t" + movie.getId() +"\n", new String(Files.readAllBytes(Paths.get("file"))));
        }

        @Test
        @DisplayName("when 0 record should igonre")
        public void whenValidInputShouldWrite() throws IOException {
            Movie movie = init();
            List<MatchingMovieToCsv> serialize = Collections.emptyList();
            File file = new File("file");
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStream out = moviesPairCsvWriter.write(fileOutputStream, serialize);

            Assertions.assertEquals("", new String(Files.readAllBytes(Paths.get("file"))));
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class lala {
        List<String> ll;
    }

    private Movie init() {
        return Movie.builder()
                .id("tt2355936")
                .length(89d)
                .year(2013)
                .genre(Collections.singletonList("Drama"))
                .directors(Collections.singletonList("Lina Chamie"))
                .actors(Arrays.asList("Davi Galdeano", "Gregório Mussatti Cesare", "Dira Paes", "Julia Weiss", "Antônia Ricca", "Marco Ricca", "Lucas Zamberlan"))
                .build();
    }

}