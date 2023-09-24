package com.example.womenssafety

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {



    lateinit var mContext: Context
    private val listContacts:ArrayList<ContactModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //lateinit var binding: FragmentHomeBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }



    override fun onCreateView(
        inflater: LayoutInflater,container:ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        //FragmentHomeBinding.inflate(inflater,container,false)
        return inflater.inflate(R.layout.fragment_home,container,false)

    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listmembers= listOf<MemberModel>(
            MemberModel("Aman",
                "Shindhipura Kanch Mandir ke Pichhe Bhej do Bam Polish ke pass",
                "34%",
                "21"),
            MemberModel(
                "Kedia",
                "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "D4D5",
                "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Ramesh",
                "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                "60%",
                "190"
            ),
            MemberModel("Aman",
                "Shindhipura Kanch Mandir ke Pichhe Bhej do Bam Polish ke pass",
                "34%",
                "21"),
            MemberModel(
                "Aman",
                "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "D4D5",
                "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Ramesh",
                "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                "60%",
                "190"
            ),

            )

        val listcontacts= listOf<ContactModel>(
            ContactModel("Aman",
                98786656),
            ContactModel("Aman",
                987866566),
            ContactModel("Aman",
                9878665662),
            ContactModel("Aman",
                987866562),
            ContactModel("Aman",
                987866232),
            ContactModel("Aman",
                987866562),
            ContactModel("Aman",
                987866532),
            ContactModel("Aman",
                987866232),

            )

        val adapter= MemberAdapter(listmembers)
        val recyclerMember=requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recyclerMember.layoutManager=LinearLayoutManager(requireContext())
        recyclerMember.adapter=adapter
        val inviteAdapter= InviteAdapter(listcontacts)
        // CoroutineScope(Dispatchers.IO).launch{

        //listContacts.clear()
        // withContext(Dispatchers.Main){
        //  inviteAdapter.notifyDatachanged()
        // }


        // listContacts.addAll(fetchContacts())
        // withContext(Dispatchers.Main){
        //inviteAdapter.notifyDatachanged()
        //}

        //  }


        val inviteRecycler=requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter=inviteAdapter
        val threeDots=requireView().findViewById<ImageView>(R.id.icon_three_dots)
        threeDots.setOnClickListener{
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
        }




    }}





//////
/* private fun fetchContacts(): ArrayList<ContactModel> {

     //Log.d("FetchContact89", "fetchContacts: start")
     val cr = requireActivity().contentResolver
     val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

     val listContacts: ArrayList<ContactModel> = ArrayList()


     if (cursor != null && cursor.count > 0) {

         while (cursor != null && cursor.moveToNext()) {
             val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)+1)
             val name =
                 cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
             val hasPhoneNumber =
                 cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

             if (hasPhoneNumber > 0) {

                 val pCur = cr.query(
                     ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                     null,
                     ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                     arrayOf(id),
                     ""
                 )

                 if (pCur != null && pCur.count > 0) {

                     while (pCur != null && pCur.moveToNext()) {

                             val phoneNum =
                             pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                             listContacts.add(ContactModel(name, phoneNum))

                     }
                     pCur.close()

                 }

             }
         }

         if (cursor != null) {
             cursor.close()
         }

     }
     //Log.d("FetchContact89", "fetchContacts: end")
     return listContacts

 }



  companion object {
      @JvmStatic
      fun newInstance() = HomeFragment()
  }
}

private fun fetchContacts(): ArrayList<ContactModel> {
      val cr=requireActivity().contentResolver
      val cursor=cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
      val listContacts:ArrayList<ContactModel> = ArrayList()
      if(cursor!=null && cursor.count>0){
          while(cursor!=null && cursor.moveToNext()){
              val id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)+1)
              val name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)+1)
              val hasPhoneNumber=cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)+1)
              if(hasPhoneNumber>0){
                  val pCur=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                      ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"=?",
                      arrayOf(id),"")
                  if(pCur!=null && pCur.count>0){
                      while(pCur!=null && pCur.moveToNext()){
                        //  val phoneNum=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)+1)

                         // listContacts.add(ContactModel(name, phoneNum))
                      }
                      if(pCur!=null){
                          pCur.close()
                      }
                  }
              }}
          if(cursor!=null){
              cursor.close()
          }


      }
      return listContacts


  }*/