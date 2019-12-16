package com.ignite.wordfinder;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.viewmodel.WordListViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class WordAdapter extends BaseExpandableListAdapter {

    final private Context context;
    final private List<WordEntity> words;
    final private HashMap<Integer, List<DefinitionEntity>> definitions;
    private Typeface typeface;
    private TextToSpeech tts;
    private String currentWord;

    WordAdapter(Context context, List<WordEntity> words,
                HashMap<Integer, List<DefinitionEntity>> definitions) {
        this.context = context;
        this.words = words;
        this.definitions = definitions;
        if (context != null) {
            typeface = ResourcesCompat.getFont(context, R.font.opensans);

            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.FRANCE);
                    }
                }
            });
        }
    }

    @Override
    public int getGroupCount() {
        return words.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        WordEntity word = this.words.get(groupPosition);
        List<DefinitionEntity> definitionEntities = this.definitions.get(word.getId());
        if (definitionEntities != null)
            return definitionEntities.size();
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.words.get(groupPosition).getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<DefinitionEntity> definitionEntities = this.definitions.get(this.words.get(groupPosition).getId());
        if (definitionEntities != null)
            return definitionEntities.get(childPosition);
        else
            return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final String word = (String) getGroup(groupPosition);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);

            holder = new ViewHolder();
            holder.wordTitle = convertView.findViewById(R.id.wordTitle);
            holder.volumeButton = convertView.findViewById(R.id.volume);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wordTitle.setTypeface(typeface, Typeface.BOLD);
        holder.wordTitle.setText(word);
        holder.volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder pronouncedString = new StringBuilder();
                pronouncedString.append(holder.wordTitle.getText()).append("\n");
                for (int i = 0; i < getChildrenCount(groupPosition); i++) {
                    DefinitionEntity definition = (DefinitionEntity) getChild(groupPosition, i);
                    pronouncedString.append(definition.getType()).append("\n").append(definition.getText()).append("\n");
                }


                if (tts.isSpeaking() && currentWord == holder.wordTitle.getText()) {
                    tts.speak("", TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    currentWord = String.valueOf(holder.wordTitle.getText());
                    tts.speak(String.valueOf(pronouncedString), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            final WordListViewModel viewModel =
                    ViewModelProviders.of((FragmentActivity) context).get(WordListViewModel.class);

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle(context.getString(R.string.delete));
                adb.setMessage(context.getString(R.string.wanna_delete) + " " + word);
                adb.setNegativeButton(context.getString(R.string.cancel), null);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.deleteByWordId(viewModel.getWordByName(word).getId());
                    }
                });
                adb.show();
                return true;
            }
        });


        return convertView;
    }


    private static class ViewHolder {
        TextView wordTitle;
        ImageButton volumeButton;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        DefinitionEntity definition = (DefinitionEntity) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView typeTv = convertView.findViewById(R.id.type);
        TextView definitionTv = convertView.findViewById(R.id.definition);
        typeTv.setText(definition.getType());
        typeTv.setTypeface(typeface);
        definitionTv.setText(definition.getText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

// --Commented out by Inspection START (17/07/19 13:23):
//    public void onPause() {
//        if (tts != null) {
//            tts.stop();
//            tts.shutdown();
//        }
//    }
// --Commented out by Inspection STOP (17/07/19 13:23)
}
