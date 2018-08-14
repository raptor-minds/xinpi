/**
 * The type Institute.
 */
public final class Institute {

    private String instituteNo;

    private String name;

    private int begin;

    private int end;

    private int length;

    /**
     * Gets institute no.
     *
     * @return the institute no
     */
    public String getInstituteNo() {
        return instituteNo;
    }

    /**
     * Sets institute no.
     *
     * @param instituteNo
     *         the institute no
     */
    public void setInstituteNo(String instituteNo) {
        this.instituteNo = instituteNo;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets begin.
     *
     * @return the begin
     */
    public int getBegin() {
        return begin;
    }

    /**
     * Sets begin.
     *
     * @param begin
     *         the begin
     */
    public void setBegin(int begin) {
        this.begin = begin;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end
     *         the end
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets length.
     *
     * @param length
     *         the length
     */
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "name : " + name + " NO: " + instituteNo + " Begin: "
                + begin + " end " + end + " length: " + length + "\n";
    }
}
