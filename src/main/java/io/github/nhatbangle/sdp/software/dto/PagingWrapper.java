package io.github.nhatbangle.sdp.software.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Data
public class PagingWrapper<T> implements Serializable {

    private Integer totalPages;
    private Long totalElements;
    private Integer number;
    private Integer size;
    private Integer numberOfElements;
    private Boolean first;
    private Boolean last;
    private List<T> content;

    @NotNull
    public <R> PagingWrapper<R> map(@NotNull Function<T, R> mapper) {
        List<T> currentContent = Objects.requireNonNullElse(this.content, Collections.emptyList());
        List<R> newContent = currentContent.stream().map(mapper).toList();

        PagingWrapper<R> obj = new PagingWrapper<>();
        obj.setTotalPages(this.totalPages);
        obj.setTotalElements(this.totalElements);
        obj.setNumber(this.number);
        obj.setSize(this.size);
        obj.setNumberOfElements(this.numberOfElements);
        obj.setFirst(this.first);
        obj.setLast(this.last);
        obj.setContent(newContent);

        return obj;
    }

    @NotNull
    public void sort(@NotNull Comparator<? super T> comparator) {
        List<T> currentContent = Objects.requireNonNullElse(this.content, Collections.emptyList());
        this.content = currentContent.stream().sorted(comparator).toList();
    }

    @NotNull
    public static <T> PagingWrapper<T> from(@NotNull Page<T> page) {
        PagingWrapper<T> obj = new PagingWrapper<>();
        obj.setTotalPages(page.getTotalPages());
        obj.setTotalElements(page.getTotalElements());
        obj.setNumber(page.getNumber());
        obj.setSize(page.getSize());
        obj.setNumberOfElements(page.getNumberOfElements());
        obj.setFirst(page.isFirst());
        obj.setLast(page.isLast());
        obj.setContent(page.toList());

        return obj;
    }

}
