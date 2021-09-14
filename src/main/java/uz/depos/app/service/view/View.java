package uz.depos.app.service.view;

public interface View {
    /**
     * Enclosing type to define member views
     */
    public static interface MemberView {
        /**
         * View for external members
         */
        public static interface External {}

        /**
         * View for internal services/uses
         */
        public static interface Internal extends External {}

        /**
         * View to define deserialization of request body for POST call. any fields
         * other than defined by this view, will be just ignored.
         */
        public static interface Post {}

        public static interface Get {}

        public static interface PUT {}
    }
}
