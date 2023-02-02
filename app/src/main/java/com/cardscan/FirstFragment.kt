package com.cardscan

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.ERROR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cardscan.databinding.FragmentFirstBinding
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val MY_SCAN_REQUEST_CODE = 101
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.scanCard.setOnClickListener {
            onScanPress(it);
        }
    }

    private fun onScanPress(v: View) {

        val scanIntent = Intent(context, CardIOActivity::class.java)

        // customize these values to suit your needs.

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false


        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === MY_SCAN_REQUEST_CODE) {
            var resultDisplayStr: String
            if (data?.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)!!) {
                //val scanResult: CreditCard = attr.data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
                val scanResult: CreditCard = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)!!

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = """Card Number: ${scanResult.redactedCardNumber}""".trimIndent()

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );
                if (scanResult.isExpiryValid) {
                    resultDisplayStr += """Expiration Date: ${scanResult.expiryMonth}/${scanResult.expiryYear} """.trimIndent()
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += """CVV has ${scanResult.cvv.length} digits."""
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr += """Postal Code: ${scanResult.postalCode} """.trimIndent()
                }
                Log.e("scan result ",resultDisplayStr)
            } else {
                resultDisplayStr = "Scan was canceled."
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
    }
}