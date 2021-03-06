package com.escolaRest.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.escolaRest.javaClient.CustomSortDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PageableResponse<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;
	
	private boolean first;
	private boolean last;
	private int totalPages;

	public PageableResponse(@JsonProperty("content") List<T> content, 
							@JsonProperty("number") int page,
							@JsonProperty("size") int size,
							@JsonProperty("totalElements") long totalElements,
							@JsonProperty("sort") @JsonDeserialize(using = CustomSortDeserializer.class) Sort sort) {
		super(content, PageRequest.of(page, size, sort), totalElements);
	}
	
	public PageableResponse() {
		super(new ArrayList<>());	
	}
	
	 public Page<T> pageImpl() {
	        return new PageImpl<>(getContent(), PageRequest.of(getNumber(),
	                getSize(), getSort()), getTotalElements());
	    }

	public PageableResponse(List<T> content, boolean first, boolean last, int totalPages) {
		super(content);
		this.first = first;
		this.last = last;
		this.totalPages = totalPages;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
