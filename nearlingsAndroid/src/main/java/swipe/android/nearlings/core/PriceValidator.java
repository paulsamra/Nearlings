package de.metagear.android.view.validation.textview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

/**
 * Validates that the <code>EditText</code> contains a string that matches the
 * given regular expression.
 */
//reviewed
public class PriceValidator extends RegexValidator {
    protected Context context;
    protected Pattern pattern;

    public PriceValidator(Context context ) {
        super(context, "^\\d{0,8}(\\.\\d{1,2})?$");

        this.context = context;
    }

    @Override
    public String getErrorMessage(String caption) {
        return "Please input a valid price!";
    }
}
