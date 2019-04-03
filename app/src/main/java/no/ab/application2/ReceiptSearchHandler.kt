package no.ab.application2

class ReceiptSearchHandler{


    fun search(receipts: List<Receipt>, word: String): List<Receipt>{
        val matches = ArrayList<Receipt>()
        receipts.forEach {
            if(it.name.contains(word)) matches.add(it)
            else if(it.description.contains(word)) matches.add(it)
        }
        return matches
    }

}