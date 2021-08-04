package uz.depos.app.service.dto;

public class ResPageable {

    private Object object;

    private Long totalElements;

    private Integer currentPage;

    private Integer totalPages;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public ResPageable() {}

    public ResPageable(Object object, Long totalElements, Integer currentPage, Integer totalPages) {
        this.object = object;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return (
            "ResPageable{" +
            "object=" +
            object +
            ", totalElements=" +
            totalElements +
            ", currentPage=" +
            currentPage +
            ", totalPages=" +
            totalPages +
            '}'
        );
    }
}
