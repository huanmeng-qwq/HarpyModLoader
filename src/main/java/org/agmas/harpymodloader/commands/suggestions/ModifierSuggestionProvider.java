package org.agmas.harpymodloader.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import org.agmas.harpymodloader.Harpymodloader;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;

import java.util.concurrent.CompletableFuture;

public class ModifierSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        // suggest all roles
        if (HMLModifiers.MODIFIERS.isEmpty()) return Suggestions.empty();

        for (Modifier role : HMLModifiers.MODIFIERS) {
            if (role != null && CommandSource.shouldSuggest(builder.getRemaining(), role.identifier().getPath())) {
                builder.suggest(role.identifier().getPath());
            }
        }
        return builder.buildFuture();
    }
}
