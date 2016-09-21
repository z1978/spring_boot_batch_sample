package com.example.batch2;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.batch2.sub.JobCompletionNotificationListener2;
import com.example.batch2.sub.PersonItemProcessor2;
import com.example.item.Person;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration2 {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Person> reader2() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("sample-data.csv"));

        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[] { "firstName", "lastName" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
            }});
        }});

        /*
        // こっちの書き方の方がわかりやすいかも

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "firstName", "lastName" });
        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
        fieldSetMapper.setTargetType(Person.class);

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<Person>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);

        */
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer2() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public PersonItemProcessor2 processor2() {
        return new PersonItemProcessor2();
    }

    @Bean
    public JobExecutionListener listener2() {
        return new JobCompletionNotificationListener2();
    }


    @Bean
    public Job importUserJob2() {
        return jobBuilderFactory.get("importUserJob2")
//                .incrementer(new RunIdIncrementer())
                .listener(listener2())
                .flow(step2())
                .end()
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<Person, Person> chunk(10)
                .reader(reader2())
                .processor(processor2())
                .writer(writer2())
                .build();
    }
}

