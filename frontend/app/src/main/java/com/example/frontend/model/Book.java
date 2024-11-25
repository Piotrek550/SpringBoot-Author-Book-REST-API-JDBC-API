package com.example.frontend.model;

public class Book {

    private String isbn;

    private String title;

    private Author author;

    public Book(String isbn, String title, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public Book() {
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Book)) return false;
        final Book other = (Book) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$isbn = this.getIsbn();
        final Object other$isbn = other.getIsbn();
        if (this$isbn == null ? other$isbn != null : !this$isbn.equals(other$isbn)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Book;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $isbn = this.getIsbn();
        result = result * PRIME + ($isbn == null ? 43 : $isbn.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        return result;
    }

    public String toString() {
        return "BookDto(isbn=" + this.getIsbn() + ", title=" + this.getTitle() + ", author=" + this.getAuthor() + ")";
    }
}