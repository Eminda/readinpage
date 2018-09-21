package ethirium.eth.utils;

public class DomainConstatns {
    public enum Status {
        COMPLETE, INPROGRESS, FAILED;

        public static Status getStatus(String status) {
            if (status != null) {
                return Status.valueOf(status);
            }
            return null;
        }
    }

}
