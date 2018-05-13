package webtools.common.databaseex;

public abstract class BasePhrase {
    public static BasePhrase parse_phrase(String[] Query) {
        StrOper oper = new StrOper();
        String left = oper.cut_word(Query);
        BasePhrase phrase = null;
        if (left != null && !left.equals("")) {
            if (left.charAt(0) == '(') {
                phrase = new MultPhrase();
                phrase.putValue(left);
            } else {
                String operator = oper.cut_word(Query);
                String right = oper.cut_word(Query);
                if (operator.equalsIgnoreCase("not")) {
                    operator += " " + oper.cut_word(Query);
                    phrase = new InPhrase();
                } else if (operator.equalsIgnoreCase("in")) {
                    phrase = new InPhrase();
                } else {
                    phrase = new CommonPhrase();
                }
                phrase.setLeft(left);
                phrase.setOper(operator);
                phrase.putValue(right);
            }
            String conj = oper.cut_word(Query);
            Query[1] = conj;
        }
        return phrase;
    }

    public abstract String getPhrase(webtools.common.URL.RequestConnection con);

    public abstract void putValue(String val);

    protected String left = null;
    protected String oper = null;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

}
