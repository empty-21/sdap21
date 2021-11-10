package shinhands.com.enumeration;

public class Route {
    public enum Response {
        BAD_RESPONSE_200("direct:bad-response-200"),
        COMMON_RESPONSE_500("direct:common-500"),
        BAD_JSONVALIDATOR_200("direct:bad-jsonvalidator-200");

        public final String label;

        private Response(String label) {
            this.label = label;
        }
    }
}