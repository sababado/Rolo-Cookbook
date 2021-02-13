package com.sababado.rolo.cookbook.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import com.sababado.rolo.cookbook.R
import java.io.File


class ContactHelper {
    companion object {
        /**
         * Start the dialer with a phone number
         * @param context current context
         * @param phoneNumber Can be formatted in any way. It will show up exactly as formatted in the dialer.
         */
        fun callPhoneNumber(context: Context, phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneNumber}")
            context.startActivity(intent)
        }
    }

    class EmailBuilder(val context: Context) {
        private var intentTitle = context.getString(R.string.send_email)
        private var uri: Uri? = null
        private var recipientEmail: String? = null
        private var subject: String? = null
        private var text: String? = null

        /**
         * If the user has multiple email clients, they will get an option
         * to pick. Set the title for
         * @param intentTitle title
         */
        fun intentTitle(intentTitle: String): EmailBuilder {
            this.intentTitle = intentTitle
            return this
        }

        /**
         * If the user has multiple email clients, they will get an option
         * to pick. Set the title for
         * @param intentTitleRes resource title
         */
        fun intentTitle(@StringRes intentTitleRes: Int): EmailBuilder {
            this.intentTitle = context.getString(intentTitleRes)
            return this
        }

        /**
         * Set the email attachment
         * @param uri of the file.
         */
        fun attachment(uri: Uri): EmailBuilder {
            this.uri = uri
            return this
        }

        /**
         * Set the email attachment
         * @param file File object that will be converted to a Uri.
         */
        fun attachment(file: File): EmailBuilder {
            this.uri = Uri.fromFile(file)
            return this
        }

        /**
         * Email address to send the email to
         * @param recipientEmail Email to send to
         */
        fun recipientEmail(recipientEmail: String): EmailBuilder {
            this.recipientEmail = recipientEmail
            return this
        }

        /**
         * Email subject
         * @param subject Email subject string
         */
        fun subject(subject: String): EmailBuilder {
            this.subject = subject
            return this
        }

        /**
         * Email subject
         * @param subjectRes Email subject string resource
         */
        fun subject(@StringRes subjectRes: Int): EmailBuilder {
            this.subject = context.getString(subjectRes)
            return this
        }

        /**
         * Email text
         * @param text Email text string
         */
        fun text(text: String): EmailBuilder {
            this.text = text
            return this
        }

        /**
         * Email text
         * @param textRes Email text string resource
         */
        fun text(@StringRes textRes: Int): EmailBuilder {
            this.text = context.getString(textRes)
            return this
        }

        fun send() {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            uri?.let {
                intent.putExtra(Intent.EXTRA_STREAM, uri)
            }
            context.startActivity(Intent.createChooser(intent, intentTitle))
        }
    }
}